package com.jojo.design.module_test

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.jojo.design.module_test.databinding.ActLoginBinding
import javax.inject.Inject


/**
 *    author : JOJO
 *    e-mail : 18510829974@163.com
 *    date   : 2018/11/30 10:25 AM
 *    desc   :
 */
class LoginActivity : AppCompatActivity() {
    @Inject
    lateinit var presenter: PresenterImpl
    private lateinit var binding:ActLoginBinding
//    @Inject
//    lateinit var helloWorld: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //普通方式
//        LoginPresenter(this).login(UserInfo(line_1,"",""))


        //Dagger line_2 注入
//        DaggerAppComponent
//                .builder()
//                .appModule(AppModule(this))
//                .build()
//                .inject(this)
//        presenter.login(UserInfo(line_1, "", ""))
//        DaggerAppComponent.builder().appModule(AppModule(this)).build().inject(this)
//        Toast.makeText(this, helloWorld, Toast.LENGTH_SHORT).show()
//        DaggerHttpComponent.builder()
//                .applicationComponent(mApplicationComponent)
//                .build()
//                .inject(this)

//        DaggerTestComponent.builder().applicationComponent(BaseApplication.mApplicationComponent).build().inject(this)

        Toast.makeText(this, presenter.getData(), Toast.LENGTH_SHORT).show()
//        startAnimation()


//        iv.setImageResource(R.drawable.anim_line)
//        val animationDrawable = iv.getDrawable() as AnimationDrawable
//        animationDrawable.start()
    }
//
//    private val mCurrentPosition = FloatArray(line_2)
//    private fun startAnimation() {
//        val layoutParams = iv_fish.layoutParams
//
//        val path = Path()//通过此对象绘制一个轨迹
//        path.moveTo(200f, 200f + layoutParams.width)//起始点
//        path.lineTo(600f, 200f + layoutParams.width)
//        path.lineTo(600f, 600f + layoutParams.width)
//        path.lineTo(200f, 600f + layoutParams.width)//终止点
//
//        //pathMeasure用来计算显示坐标
//        val pathMeasure = PathMeasure(path, true)
//
//        //属性动画加载
//        val valueAnimator = ValueAnimator.ofFloat(0F, pathMeasure.length)
//
//        //设置动画时长
//        valueAnimator.duration = 5000
//
//        //加入差值器
//        valueAnimator.interpolator = LinearInterpolator()
//
//        //设置无限次循环
//        valueAnimator.repeatCount = ValueAnimator.INFINITE
//        //添加监听
//        valueAnimator.addUpdateListener { animation ->
//            //获取当前位置
//            val value = animation.animatedValue as Float
//            //boolean getPosTan(float distance, float[] pos, float[] tan) ：
//            //传入一个距离distance(0<=distance<=getLength())，然后会计算当前距
//            // 离的坐标点和切线，pos会自动填充上坐标
//            pathMeasure.getPosTan(value, mCurrentPosition, null)
//
//            //设置视图坐标
//            iv_fish.x = mCurrentPosition[0]
//            iv_fish.y = mCurrentPosition[line_1]
//        }
//
//        valueAnimator.start()
//    }


}