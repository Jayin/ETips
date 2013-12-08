package com.meizhuo.etips.animations;

import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.graphics.Camera;
import android.graphics.Matrix;

/**
 * An animation that rotates the view on the Y axis between two specified angles.
 * This animation also adds a translation on the Z axis (depth) to improve the effect.
 */
public class Rotate3dAnimation extends Animation {
    private final float mFromDegrees;
    private final float mToDegrees;
    private final float mCenterX;
    private final float mCenterY;
    /** Z轴上最大深度。 暂时这里不需要用到*/  
    private final float mDepthZ;      
    private final boolean mReverse;
    private Camera mCamera;
    private final int type;
    public final static int FLAG_POSITIVE =  1; //页面旋转标志:正面旋转至反面， 
    public final static int FLAG_NEGATIVE =  -1;//页面旋转标志:反面旋转至正面 

    /**
     * Creates a new 3D rotation on the Y axis. The rotation is defined by its
     * start angle and its end angle. Both angles are in degrees. The rotation
     * is performed around a center point on the 2D space, definied by a pair
     * of X and Y coordinates, called centerX and centerY. When the animation
     * starts, a translation on the Z axis (depth) is performed. The length
     * of the translation can be specified, as well as whether the translation
     * should be reversed in time.
     *
     * @param fromDegrees the start angle of the 3D rotation
     * @param toDegrees the end angle of the 3D rotation
     * @param centerX the X center of the 3D rotation
     * @param centerY the Y center of the 3D rotation
     * @param reverse true if the translation should be reversed, false otherwise// mReverse的意义在于，旋转完成时，可以对界面进行放缩，true 的时候
     * 不过这里不需要了，所以也注释掉                             
     *       
     * @param mtype FLAG_POSITIVE =  1; //页面旋转标志:正面旋转至反面，;FLAG_NEGATIVE =  -1;//页面旋转标志:反面旋转至正面 
     *                                   正面 翻转过半的情况下，为保证数字仍为可读的文字而非镜面效果的文字，需翻转180度。  
     */
    protected Rotate3dAnimation(float fromDegrees, float toDegrees,
            float centerX, float centerY, float depthZ, boolean reverse,int mtype) {
        mFromDegrees = fromDegrees;
        mToDegrees = toDegrees;
        mCenterX = centerX;
        mCenterY = centerY;
        mDepthZ = depthZ;
        mReverse = reverse;
        type = mtype;
    }
    /**
     * Creates a new 3D rotation on the Y axis. The rotation is defined by its
     * start angle and its end angle. Both angles are in degrees. The rotation
     * is performed around a center point on the 2D space, definied by a pair
     * of X and Y coordinates, called centerX and centerY. When the animation
     * starts, a translation on the Z axis (depth) is performed. The length
     * of the translation can be specified, as well as whether the translation
     * should be reversed in time.
     * @param fromDegrees the start angle of the 3D rotation
     * @param toDegrees the end angle of the 3D rotation
     * @param centerX the X center of the 3D rotation
     * @param centerY  the Y center of the 3D rotation
     * @param mtype FLAG_POSITIVE =  1; //页面旋转标志:正面旋转至反面，;FLAG_NEGATIVE =  -1;//页面旋转标志:反面旋转至正面 
     *                                   正面 翻转过半的情况下，为保证数字仍为可读的文字而非镜面效果的文字，需翻转180度。  
     */
    public Rotate3dAnimation(float fromDegrees, float toDegrees,
            float centerX, float centerY, int mtype) {
        this(fromDegrees,toDegrees,centerX,centerY,310.0f,true,mtype);
    }

    @Override
    public void initialize(int width, int height, int parentWidth, int parentHeight) {
        super.initialize(width, height, parentWidth, parentHeight);
        mCamera = new Camera();
    }

    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {
        final float fromDegrees = mFromDegrees;
        float degrees = fromDegrees + ((mToDegrees - fromDegrees) * interpolatedTime);
        
        final float centerX = mCenterX;
        final float centerY = mCenterY;
        final Camera camera = mCamera;
        if(type == FLAG_POSITIVE){
            boolean overHalf = (interpolatedTime > 0.5f);  
            if (overHalf) {  
                // 翻转过半的情况下，为保证数字仍为可读的文字而非镜面效果的文字，需翻转180度。   
            	degrees = degrees - 180;  
            }  
        }
        final Matrix matrix = t.getMatrix();
      
        camera.save();
//  mReverse 
//        if (mReverse) {
//            camera.translate(0.0f, 0.0f, mDepthZ * interpolatedTime);
//        } else {
//            camera.translate(0.0f, 0.0f, mDepthZ * (1.0f - interpolatedTime));
//        }
        camera.rotateY(degrees);
        camera.getMatrix(matrix);
        camera.restore();

        matrix.preTranslate(-centerX, -centerY);
        matrix.postTranslate(centerX, centerY);
    }
}
