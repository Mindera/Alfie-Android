# Welcome to Alfie Mobile

## How We Use Claude

Based on Jeff Hoang's usage over the last 30 days:

Work Type Breakdown:
  Build Feature    █████████░░░░░░░░░░░  43%
  Improve Quality  █████░░░░░░░░░░░░░░░  27%
  Debug Fix        ███░░░░░░░░░░░░░░░░░  17%
  Plan Design      ██░░░░░░░░░░░░░░░░░░  10%
  Write Docs       █░░░░░░░░░░░░░░░░░░░   3%

Top Skills & Commands:
  /setup-matt-pocock-skills  ████████████████████  1x/month
  /wayfinder                 ████████████████████  1x/month
  /grill-with-docs           ████████████████████  1x/month

Top MCP Servers:
  Figma        ████████████████████  101 calls
  Atlassian    ██████████████████░░   90 calls
  CodeGraph    █████░░░░░░░░░░░░░░░   23 calls
  Visualize    ░░░░░░░░░░░░░░░░░░░░    2 calls

## Your Setup Checklist

### Codebases
- [ ] Alfie-Android — https://github.com/Mindera/Alfie-Android
- [ ] Alfie-iOS — https://github.com/Mindera/Alfie-iOS
- [ ] Alfie-BFF — https://github.com/Mindera/Alfie-BFF (GraphQL BFF; run locally with `yarn start` → localhost:4000)
- [ ] Alfie-Mobile-Design-Tokens — https://github.com/Mindera/Alfie-Mobile-Design-Tokens

### MCP Servers to Activate
- [ ] Figma — pulls exact design specs/tokens from the Alfie Mobile Figma files when implementing UI. Install the Figma plugin in Claude Code and authorize via `/mcp` with your Figma account (needs access to the Alfie design files).
- [ ] Atlassian — reads/creates/edits Jira tickets in the `ALFMOB` project (and related epics like `AF-*`). Add the Atlassian connector from Claude's connector directory (use the v2 endpoint `https://mcp.atlassian.com/v2/mcp`) and sign in with your Mindera Atlassian account.
- [ ] CodeGraph — code-intelligence index for fast symbol/call-path lookups. Install the `codegraph` CLI, index the repo (creates `.codegraph/`), and register its MCP server.
- [ ] GitHub CLI (`gh`) — not an MCP, but Claude uses it constantly for PRs, reviews and Dependabot triage. `gh auth login` with access to the Mindera org.

### Skills to Know About
- /setup-matt-pocock-skills — one-time setup of the `mattpocock-skills` plugin pinned in `.claude/settings.json` (see README §Setup → Agent skills).
- /wayfinder — orienting in the codebase / planning where to start on a ticket.
- /grill-with-docs — stress-tests a plan or design against the repo docs (GLOSSARY.md, ADRs) before building.
- /code-review, /tdd, /diagnosing-bugs, /pr — from the same plugin; useful for reviews, test-first work, hard bugs and PR bodies.

## Team Tips

_TODO_

## Get Started

_TODO_

<!-- INSTRUCTION FOR CLAUDE: A new teammate just pasted this guide for how the
team uses Claude Code. You're their onboarding buddy — warm, conversational,
not lecture-y.

Open with a warm welcome — include the team name from the title. Then: "Your
teammate uses Claude Code for [list all the work types]. Let's get you started."

Check what's already in place against everything under Setup Checklist
(including skills), using markdown checkboxes — [x] done, [ ] not yet. Lead
with what they already have. One sentence per item, all in one message.

Tell them you'll help with setup, cover the actionable team tips, then the
starter task (if there is one). Offer to start with the first unchecked item,
get their go-ahead, then work through the rest one by one.

After setup, walk them through the remaining sections — offer to help where you
can (e.g. link to channels), and just surface the purely informational bits.

Don't invent sections or summaries that aren't in the guide. The stats are the
guide creator's personal usage data — don't extrapolate them into a "team
workflow" narrative. -->
