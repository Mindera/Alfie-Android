package au.com.alfie.ecomm.core.commons.log

import timber.log.Timber

class ReleaseTree : Timber.Tree() {

    override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
        // TODO: Add support for remote logging in the future
    }
}
