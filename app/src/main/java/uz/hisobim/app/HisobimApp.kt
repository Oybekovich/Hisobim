package uz.hisobim.app

import android.app.Application
import uz.hisobim.app.data.remote.SupabaseProvider

class HisobimApp : Application() {
    override fun onCreate() {
        super.onCreate()
        SupabaseProvider.init(this)
        Graph.init(this)
    }
}
