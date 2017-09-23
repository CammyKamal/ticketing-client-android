# Splasher
Android splash screen with cool animations

## Screenshots

<p float="left">
  <img src="https://s26.postimg.org/4couky10p/one.gif" width="100" />
  <img src="https://s26.postimg.org/781xrt50p/two.gif" width="100" /> 
  <img src="https://s26.postimg.org/q1sc2jsmh/for.gif" width="100" />
  <img src="https://s26.postimg.org/h81flg5nt/three.gif" width="100" />
</p>

## Credits

<br>
This library use :  <br>
1 . <a href="https://github.com/ozodrukh/CircularReveal">CircularReveal</a> <br>
2 . <a href="https://github.com/daimajia/AndroidViewAnimations">AndroidViewAnimations</a> <br>

# Installing
Add this to your build.grade:

```
	repositories {
	//...
        maven { url "https://jitpack.io" }
        }
```
and then in dependencies:

```
dependencies {
	         compile 'com.github.wailbabou:Splasher:0.1.2'
	}
```

# How to use it
```java
public class YourActivity extends SplasherActivity {
...... code here .....
}
```
we have 5 type of reveal animation : 
* START_TOP_LEFT
* START_TOP_RIGHT
* START_BOTTOM_LEFT_
* START_BOTTOM_RIGHT
* START_CENTER
```java
 @Override
    public void initSplasher(SplasherConfig config) {
        config.setReveal_start(Const.START_TOP_LEFT) // anitmation type ..
                //---------------
                .setAnimationDuration(5000) // Reveal animation duration ..
                //---------------
                .setLogo(R.drawable.web_hi_res_512) // logo src..
                .setLogo_animation(Techniques.BounceIn) // logo animation ..
                .setAnimationLogoDuration(2000) // logo animation duration ..
                .setLogoWidth(500) // logo width ..
                //---------------
                .setTitle("Splasher Example") // title ..
                .setTitleColor(Color.parseColor("#ffffff")) // title color ..
                .setTitleAnimation(Techniques.Bounce) // title animation ( from Android View Animations ) ..
                .setTitleSize(24) // title text size ..
                //---------------
                .setSubtitle("Enjoy with this library") // subtitle
                .setSubtitleColor(Color.parseColor("#ffffff")) // subtitle color
                .setSubtitleAnimation(Techniques.FadeIn) // subtitle animation (from Android View Animations) ..
                .setSubtitleSize(16) // subtitle text size ..
                //---------------
                .setSubtitleTypeFace(Typeface.createFromAsset(getAssets(),"diana.otf")) // subtitle font type ..
                .setTitleTypeFace(Typeface.createFromAsset(getAssets(),"stc.otf")); // title font type ..

    }
```
and finally 

```java
 @Override
    public void onSplasherFinished() {
        Toast.makeText(this, "Go to the next activity", Toast.LENGTH_SHORT).show();
    }
```
## Use your custom layout 

![alt text](https://s26.postimg.org/cysmieii1/five.gif)

```java
... code ....
 config.setCustomView(R.layout.custom_view)
                .setReveal_start(Const.START_CENTER)
                .setAnimationDuration(5000);
```
and to get the view , just like that :

```java
getCustomView().findViewById(R.id.yourId);
```

# LICENSE

```
MIT License

Copyright (c) 2017 wailbabou

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
```
