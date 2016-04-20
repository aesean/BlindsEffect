# BlindsEffect
对任意图片或者View实现百叶窗切换特效
实现思路很简单，没什么难度。就是拿到两个Bitmap（可以是View也可以是资源文件），然后裁剪两个文件，然后通过裁减后的Bitmap，创建ImageView，然后添加属性动画。
1、拿到两个Bitmap
2、裁减Bitmap
3、创建ImageView
4、利用属性动画实现动效
