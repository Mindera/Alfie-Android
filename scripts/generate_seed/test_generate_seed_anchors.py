"""
Tests for generate_seed_anchors.py.

Run from the repo root:
  python3 -m unittest discover -s scripts/generate_seed
"""
import json
import os
import shutil
import subprocess
import sys
import tempfile
import unittest
from pathlib import Path

import generate_seed_anchors as gen

TESTDATA = Path(__file__).resolve().parent / "testdata"
# Alfie-BFF main's seed-anchors.json, copied verbatim.
BFF_MAIN_ANCHORS = TESTDATA / "seed-anchors.json"
# The hand-written SeedAnchors.kt from #93, under the generated header for FAKE_COMMIT.
EXPECTED_FROM_BFF_MAIN = TESTDATA / "SeedAnchors.expected.kt.txt"
FAKE_COMMIT = "0123456789abcdef0123456789abcdef01234567"
SCRIPT = Path(__file__).resolve().parent / "generate_seed_anchors.py"


class RenderTest(unittest.TestCase):

    def test_bff_main_anchors_render_as_the_hand_written_stand_in(self):
        anchors = json.loads(BFF_MAIN_ANCHORS.read_text(encoding="utf-8"))

        kotlin = gen.render(anchors, FAKE_COMMIT)

        self.maxDiff = None
        self.assertEqual(EXPECTED_FROM_BFF_MAIN.read_text(encoding="utf-8"), kotlin)

    def test_kotlin_string_metacharacters_are_escaped(self):
        kotlin = gen.render({"odd": 'a"b$c\\d', "odds": ['x"', "$y"]}, FAKE_COMMIT)

        self.assertIn(r'const val odd = "a\"b\$c\\d"', kotlin)
        self.assertIn(r'val odds = listOf(' '\n' r'        "x\"",' '\n' r'        "\$y"', kotlin)

    def test_an_anchor_kotlin_cannot_express_fails_naming_it(self):
        cases = {
            "number": ({"products": {"count": 3}}, "products.count"),
            "empty list": ({"products": {"ids": []}}, "products.ids"),
            "non-string item": ({"ids": ["a", 1]}, "ids"),
            "not an identifier": ({"collections": {"new-in": "new-in"}}, "collections.new-in"),
            "hard keyword": ({"collections": {"object": "x"}}, "collections.object"),
            "trailing newline": ({"collections": {"women\n": "women"}}, "collections.women\n"),
        }
        for case, (anchors, path) in cases.items():
            with self.subTest(case), self.assertRaisesRegex(gen.AnchorError, f"'{path}'"):
                gen.render(anchors, FAKE_COMMIT)


class CliTest(unittest.TestCase):
    """Runs the script as a shell would, against a throwaway Alfie-BFF git checkout."""

    def setUp(self):
        tmp = tempfile.TemporaryDirectory()
        self.addCleanup(tmp.cleanup)
        self.bff = Path(tmp.name) / "Alfie-BFF"
        self.anchors_json = self.bff / "src" / "mock" / "fixtures" / "seed-anchors.json"
        self.anchors_json.parent.mkdir(parents=True)
        shutil.copy(BFF_MAIN_ANCHORS, self.anchors_json)
        self._git("init", "--quiet", "--initial-branch=main")
        self._git("add", ".")
        self._git("commit", "--quiet", "-m", "seed anchors")
        self.origin = Path(tmp.name) / "origin.git"
        self.output = Path(tmp.name) / "SeedAnchors.kt"

    def _publish_main(self):
        """Gives the BFF checkout an `origin` whose main is the checkout's HEAD."""
        subprocess.run(["git", "init", "--quiet", "--bare", str(self.origin)], check=True)
        self._git("remote", "add", "origin", str(self.origin))
        self._git("push", "--quiet", "origin", "HEAD:refs/heads/main")

    def _git(self, *args):
        return subprocess.run(
            ["git", "-c", "user.name=test", "-c", "user.email=test@example.com",
             "-c", "commit.gpgsign=false", "-c", "core.hooksPath=/dev/null", *args],
            cwd=self.bff, check=True, capture_output=True, text=True,
        ).stdout.strip()

    def _run(self, *args):
        env = {**os.environ, "ALFIE_BFF_PATH": str(self.bff)}
        return subprocess.run(
            [sys.executable, str(SCRIPT), "--output", str(self.output), *args],
            env=env, capture_output=True, text=True,
        )

    def test_writes_seed_anchors_recording_the_bff_commit(self):
        result = self._run()

        self.assertEqual(0, result.returncode, result.stderr)
        expected = EXPECTED_FROM_BFF_MAIN.read_text(encoding="utf-8").replace(
            FAKE_COMMIT, self._git("rev-parse", "HEAD")
        )
        self.assertEqual(expected, self.output.read_text(encoding="utf-8"))

    def _add_anchor(self, group, name, value):
        anchors = json.loads(self.anchors_json.read_text(encoding="utf-8"))
        anchors[group][name] = value
        self.anchors_json.write_text(json.dumps(anchors, indent=2), encoding="utf-8")

    def test_an_anchor_added_to_the_json_is_added_on_rerun(self):
        self._run()
        self._add_anchor("products", "lowStock", "harbour-wool-scarf")

        result = self._run()

        self.assertEqual(0, result.returncode, result.stderr)
        kotlin = self.output.read_text(encoding="utf-8")
        self.assertIn(
            '        const val noMetafieldsNoSizeGuide = "ember-waxed-field-jacket"\n'
            '        const val lowStock = "harbour-wool-scarf"\n'
            "    }\n",
            kotlin,
        )
        head = self._git("rev-parse", "HEAD")
        self.assertIn(f"@ {head} with uncommitted changes to seed-anchors.json\n", kotlin)


    def test_check_passes_when_the_file_matches_the_bff(self):
        self._run()

        result = self._run("--check")

        self.assertEqual(0, result.returncode, result.stderr)
        self.assertIn("up to date", result.stdout)


    def test_check_fails_without_writing_when_the_bff_has_a_new_anchor(self):
        self._run()
        committed = self.output.read_text(encoding="utf-8")
        self._add_anchor("searchTerms", "accented", "café")

        result = self._run("--check")

        self.assertEqual(1, result.returncode, result.stderr)
        self.assertEqual(committed, self.output.read_text(encoding="utf-8"))
        self.assertIn("stale", result.stdout)
        self.assertIn('+        const val accented = "café"', result.stdout)
        self.assertIn("python3 scripts/generate_seed/generate_seed_anchors.py", result.stdout)


    def test_check_passes_when_the_bff_moved_on_without_touching_the_anchors(self):
        self._run()
        self._git("commit", "--quiet", "--allow-empty", "-m", "unrelated BFF change")

        result = self._run("--check")

        self.assertEqual(0, result.returncode, result.stdout)

    def test_a_missing_anchor_json_is_an_error_naming_alfie_bff_path(self):
        self.anchors_json.unlink()

        for args in [(), ("--check",)]:
            with self.subTest(args=args):
                result = self._run(*args)

                self.assertEqual(2, result.returncode, result.stdout)
                self.assertIn("ALFIE_BFF_PATH", result.stderr)
                self.assertFalse(self.output.exists())

    def test_anchor_json_without_a_top_level_object_is_an_error_not_staleness(self):
        self.anchors_json.write_text("[1]", encoding="utf-8")

        result = self._run("--check")

        self.assertEqual(2, result.returncode, result.stderr)
        self.assertIn("top level", result.stderr)


    def _check_warnings(self, *args):
        """stderr of a `--check` that must still pass: the warnings are advisory."""
        self._run()
        result = self._run("--check", *args)
        self.assertEqual(0, result.returncode, result.stderr)
        self.assertIn("up to date", result.stdout)
        return result.stderr

    def test_no_warning_when_the_bff_is_on_origin_main(self):
        self._publish_main()

        self.assertEqual("", self._check_warnings())

    def test_a_detached_head_on_origin_main_counts_as_main(self):
        self._publish_main()
        self._git("checkout", "--quiet", "--detach")

        self.assertEqual("", self._check_warnings())

    def test_warns_when_the_bff_is_on_another_branch(self):
        self._publish_main()
        self._git("checkout", "--quiet", "-b", "feature/new-anchor")

        self.assertIn("is on feature/new-anchor, not main", self._check_warnings())

    def test_warns_when_the_bff_is_on_a_detached_head_off_main(self):
        self._publish_main()
        self._git("commit", "--quiet", "--allow-empty", "-m", "unmerged")
        self._git("checkout", "--quiet", "--detach")

        self.assertIn("is on a detached HEAD, not main", self._check_warnings())

    def test_warns_when_the_bff_is_behind_origin_main(self):
        self._publish_main()
        self._git("commit", "--quiet", "--allow-empty", "-m", "merged upstream")
        self._git("push", "--quiet", "origin", "HEAD:refs/heads/main")
        self._git("reset", "--quiet", "--hard", "HEAD~1")

        self.assertIn("is behind origin/main", self._check_warnings())

    def test_warns_when_origin_main_cant_be_read(self):
        self.assertIn("Couldn't read origin/main", self._check_warnings())

    def test_no_branch_warning_skips_the_branch_check_for_callers_that_make_it(self):
        self._git("checkout", "--quiet", "-b", "feature/new-anchor")  # and no origin to read

        self.assertEqual("", self._check_warnings("--no-branch-warning"))


if __name__ == "__main__":
    unittest.main()
