package zhengzhou.individual.myapplication

import android.view.View
import kotlinx.coroutines.newFixedThreadPoolContext
import org.jetbrains.anko.*;
import org.jetbrains.anko.AnkoContext
import org.jetbrains.anko.verticalLayout

class MainUI : AnkoComponent<MainActivity> {
    override fun createView(ui: AnkoContext<MainActivity>): View {
        return with(ui) {
            verticalLayout{
                textView {
                    text = "Hello Anko"
                }
                button {
                    text = "Anko DSL leann"
                    setOnClickListener{
                        coroutine()
                    }
                }
            }
        }
    }

    /**
     * 启动 1000 协程
     */
    fun coroutine() {
        val pool = newFixedThreadPoolContext(10, "coroutine")
        for (x in 0..1000) {
            pool.run {
               println("here")
            }
        }
    }
}