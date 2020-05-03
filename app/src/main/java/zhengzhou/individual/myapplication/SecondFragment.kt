package zhengzhou.individual.myapplication

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.fragment.findNavController
import okhttp3.*
import java.io.IOException
import java.util.concurrent.Executors
import kotlinx.android.synthetic.main.fragment_second.*
import kotlinx.coroutines.*

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class SecondFragment : Fragment() {
    val url = "https://www.google.com/"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_second, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (view.findViewById(R.id.button) as Button).setOnClickListener({ coroutine() })
        view.findViewById<Button>(R.id.button3).setOnClickListener({ td() })
        view.findViewById<Button>(R.id.button_second).setOnClickListener {
            findNavController().navigate(R.id.action_SecondFragment_to_FirstFragment)
        }
    }


    /**
     * 启动 1000 协程
     */
    fun coroutine() {
        val pool = newFixedThreadPoolContext(10, "coroutine")
        for (x in 0..1000) {
            pool.run {
                write_sp("Coroutine", x.toString(), x)
            }
        }
        updateUICoroutine()
    }

    /**
     * 启动 1000 线程
     */
    fun td() {
        val pool = Executors.newFixedThreadPool(10)
        for (x in 0..1000) {
            pool.run {
                write_sp("Thread", x.toString(), x)
            }
        }
        updateUIThread()
    }

    fun write_sp(way: String, name: String, value: Int) {
        val sp = this.activity!!.applicationContext.getSharedPreferences(
            "Hello", Context.MODE_PRIVATE
        );

        sp.edit().putInt(name, value).apply()
        val count = sp.getInt(name, 0)
        println("方法 $way + $name + $value")
    }

    fun updateUIThread() {
        val client = OkHttpClient()
        val request = Request.Builder().url(url).build()
        client.newCall(request).enqueue(object : Callback { // enquene 以为 着异步
            override fun onFailure(call: Call, e: IOException) {
                textView.post {
                    textView.text = "Hello, 老子是线程， 失败"; // post 到 主线程
                }
            }

            override fun onResponse(call: Call, response: Response) {
                if (response!!.isSuccessful) {
                    textView.post {
                        textView.text = "Hello, 老子是线程，成功"
                    }
                }
            }
        })
    }

    fun updateUICoroutine() {
        val client = OkHttpClient()
        val request = Request.Builder().url(url).build()
        GlobalScope.launch(Dispatchers.Default) {
            val response = client.newCall(request).execute()
            if (response!!.isSuccessful) {
                GlobalScope.launch(Dispatchers.Main){
                    textView.text = "Hello, 老子是协程,成功"
                }
            } else {
                GlobalScope.launch(Dispatchers.Main){
                    textView.text = "Hello, 老子是协程，失败"
                }
            }
        }
    }
}
