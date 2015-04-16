/*
 * Bifröst - A camera-to-speech system for Android
 * Copyright (C) 2015 Jean-Marie Favreau <jeanmarie.favreau@free.fr>
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or
 * any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301  USA
 */

package info.jmfavreau.bifrostcore.imageprocessing;

import android.graphics.Bitmap;
import android.os.Environment;
import android.util.Log;

import junit.framework.Assert;

import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.CvException;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfFloat;
import org.opencv.core.MatOfInt;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

/**
 * Created by Jean-Marie Favreau on 16/04/15.
 */
public class ImageToColor {

    private void saveImage(Mat mat) {
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "MyAppDir");

        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.e("bifrostcore", "failed to create directory");
                return;
            }
        }

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile;
        mediaFile = new File(mediaStorageDir.getPath() + File.separator + "bifrostcore_" + timeStamp + ".png");
        Log.w("bifrostcore", "save image to " + mediaFile.toString());
        Highgui.imwrite(mediaFile.toString(), mat);
    }

    public Scalar process(Bitmap bmp) {
        // convert the image to OpenCV format
        Log.d("bifrostcore", "create original image");
        Mat original_alpha = new Mat ();
        Assert.assertNotNull(original_alpha);
        Utils.bitmapToMat(bmp, original_alpha);
        // remove alpha
        Mat original = new Mat();
        Imgproc.cvtColor(original_alpha, original, Imgproc.COLOR_RGBA2RGB, 0);
        Log.d("bifrostcore", "image size: " + String.valueOf(original.total()));

        // compute an ROI
        Mat roi = compute_roi(original);

        Log.d("bifrostcore", "smooth image");
        // smooth the image
        Mat smoothed = smooth_image(original);

        Log.d("bifrostcore", "convert to hsv");
        Mat hsv = toHSV(smoothed);

        Log.d("bifrostcore", "extract main region");
        // extract main region using histogram
        Mat main_region = extract_main_region(hsv, roi);

        // threshold to preserve only the most significant regions
        Mat main_region_threshold = threshold_mask(main_region);
        saveImage(main_region_threshold);

        Log.d("bifrostcore", "return mean value");
        // return the mean value
        return Core.mean(original, main_region_threshold);
    }

    private Mat compute_roi(Mat original) {
        Mat roi = new Mat();
        Imgproc.cvtColor(original, roi, Imgproc.COLOR_BGR2GRAY, 0);
        roi.setTo(new Scalar(0, 0, 0));
        int x = original.width();
        int y = original.height();
        int cx = x / 2;
        int cy = y / 2;
        int r = Math.min(cx, cy) * 2 / 3;
        Core.circle(roi, new Point(cx, cy), r, new Scalar(255, 255, 255), -1, 8, 0);
        return roi;
    }

    private Mat threshold_mask(Mat main_region) {
        Mat result = main_region.clone();

        Imgproc.threshold(main_region, result, Core.mean(main_region).val[0], 255, Imgproc.THRESH_TOZERO);

        return result;
    }

    private Mat toHSV(Mat img) {
        Mat hsv = new Mat();
        Imgproc.cvtColor(img, hsv, Imgproc.COLOR_BGR2HSV);
        return hsv;
    }

    private Mat extract_main_region(Mat img, Mat roi) {
        Mat hist = new Mat();
        int h_bins = 30;
        int s_bins = 32;
        MatOfInt mHistSize = new MatOfInt (h_bins, s_bins);

        MatOfFloat mRanges = new MatOfFloat(0, 179, 0, 255);
        MatOfInt mChannels = new MatOfInt(0, 1);

        Imgproc.calcHist(Arrays.asList(img), mChannels, roi, hist, mHistSize, mRanges, false);

        Core.normalize(hist, hist, 0, 255, Core.NORM_MINMAX, -1, new Mat());

        Mat backproj = new Mat();
        Imgproc.calcBackProject(Arrays.asList(img), mChannels, hist, backproj, mRanges, 1);

        Log.w("bifrostcore", "Number of pixels in the biggest region: " + String.valueOf(Core.countNonZero(backproj)));
        return backproj.mul(roi);
    }

    private Mat smooth_image(Mat img) {
        Mat smoothed = img.clone();
        int diam = 20; // TODO: adjust this value depending of the image size
        int std_dev_color = 80; // TODO: fix these values
        int std_dev_space = 80;
        try {
            Imgproc.bilateralFilter(img, smoothed, diam, std_dev_color, std_dev_space);
        }
        catch (CvException e) {
            Log.e("bifrostcore", e.getMessage());
        }
        return smoothed;
    }
}
