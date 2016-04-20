# BlindsEffect
对任意图片或者View实现百叶窗切换特效
实现思路很简单，没什么难度。就是拿到两个Bitmap（可以是View也可以是资源文件），然后裁剪两个文件，然后通过裁减后的Bitmap，创建ImageView，然后添加属性动画。
1、拿到两个Bitmap
2、裁减Bitmap
3、创建ImageView
4、利用属性动画实现动效

因为Bitmap直接就是原始的，没有做任何缩放裁剪，内存占用相当高。整体代码很简单，这个项目看看就行了，只是提供一种思路。
在不同分辨率的屏幕上，裁剪出来的图有时候会出现类似效果图中的无法拼接到一起到情况，暂时不清楚原因。
# 效果图
![效果图](https://github.com/aesean/BlindsEffect/blob/master/ezgif.com-video-to-gif.gif)
