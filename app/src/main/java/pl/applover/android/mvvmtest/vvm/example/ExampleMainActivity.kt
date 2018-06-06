package pl.applover.android.mvvmtest.vvm.example

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import kotlinx.android.synthetic.main.example_activity_main.*
import pl.applover.android.mvvmtest.R
import pl.applover.android.mvvmtest.util.extensions.goToActivity
import pl.applover.android.mvvmtest.util.other.DaggerAppCompatActivity
import pl.applover.android.mvvmtest.vvm.example.next_example.NextExampleActivity


class ExampleMainActivity : DaggerAppCompatActivity() {


    internal lateinit var viewModel: ExampleMainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.example_activity_main)
        viewModel = ViewModelProviders.of(this).get(ExampleMainViewModel()::class.java)
        viewModel.someEvent.observe(this, Observer { event -> println(event?.getContentIfNotHandled()) })
        viewModel.title.observe(this, Observer { title -> textViewHelloWorld.text = title })

        textViewHelloWorld.setOnClickListener {
            println("Go to next Activity")
            goToNextActivity()
        }
    }

    private fun goToNextActivity() {
        goToActivity(NextExampleActivity::class.java, true)
    }

    override fun onResume() {
        super.onResume()
        viewModel.activityOnResume()
    }

}