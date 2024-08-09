package com.example.animatrx;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageView;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private ImageView imageView;
    private AnimationManager animationManager;
    private Handler handler = new Handler();
    private Runnable animationRunnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView = findViewById(R.id.imageView);
        Button button = findViewById(R.id.button);

        // Wait until the layout is fully loaded to calculate screen dimensions
        imageView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                imageView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                float maxX = imageView.getRootView().getWidth(); // Full screen width
                float maxY = imageView.getRootView().getHeight(); // Full screen height
                animationManager = new AnimationManager(maxX, maxY); // Initialize AnimationManager
                startAnimations();
            }
        });

        // Change the image when the button is clicked
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animationManager.nextImage();
                updateImage();
            }
        });
    }

    private void startAnimations() {
        animationRunnable = new Runnable() {
            @Override
            public void run() {
                animateImage(); // Perform the image animation
                handler.postDelayed(this, 4000); // Repeat every 4 seconds
            }
        };
        handler.post(animationRunnable); // Start the animation loop
    }

    private void animateImage() {
        // Move and rotate the image to a new position
        float targetX = animationManager.getxOffset();
        float targetY = animationManager.getyOffset();

        imageView.animate()
                .x(targetX)
                .y(targetY)
                .rotationBy(-360)
                .setDuration(1000) // Duration for movement and rotation
                .setInterpolator(new AccelerateDecelerateInterpolator()) // Smooth in and out
                .withEndAction(new Runnable() {
                    @Override
                    public void run() {
                        // Zoom out and change the image
                        imageView.animate()
                                .scaleX(0f)
                                .scaleY(0f)
                                .setDuration(900) // Zoom-out duration
                                .withEndAction(new Runnable() {
                                    @Override
                                    public void run() {
                                        // Change the image after zooming out
                                        animationManager.nextImage();
                                        updateImage();

                                        // Zoom in and return to the original position with a rotation
                                        imageView.animate()
                                                .x(animationManager.getOriginalX())
                                                .y(animationManager.getOriginalY())
                                                .scaleX(1f)
                                                .scaleY(1f)
                                                .rotationBy(360)
                                                .setDuration(900) // Zoom-in and return duration
                                                .setInterpolator(new AccelerateDecelerateInterpolator()) // Smooth in and out
                                                .start();
                                    }
                                })
                                .start();
                    }
                })
                .start();
    }

    private void updateImage() {
        int resId = getResources().getIdentifier(animationManager.getCurrentImage(), "drawable", getPackageName());
        imageView.setImageResource(resId);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(animationRunnable); // Stop the animation loop
        animationManager.stopTimers(); // Stop position updates
    }

    public class AnimationManager {
        private float xOffset = 0;
        private float yOffset = 0;
        private int imageId = 0;

        private Handler timerHandler = new Handler();
        private Runnable offsetRunnable;

        private float maxX;
        private float maxY;
        private float originalX;
        private float originalY;
        private Random random = new Random();

        private String[] imageArray = {"diwali_fireworks", "colourfull_fan", "shared1", "shared2", "shared3", "shared4"};

        public AnimationManager(float maxX, float maxY) {
            this.maxX = maxX;
            this.maxY = maxY;
            this.originalX = imageView.getX(); // Store the original X position
            this.originalY = imageView.getY(); // Store the original Y position
            startTimers(); // Start updating positions
        }

        private void startTimers() {
            offsetRunnable = new Runnable() {
                @Override
                public void run() {
                    moveToRandomPosition(); // Generate a new random position
                    timerHandler.postDelayed(this, 300); // Update position every 300ms
                }
            };
            timerHandler.post(offsetRunnable);
        }

        private void moveToRandomPosition() {
            // Define image dimensions
            float imageWidth = imageView.getWidth();
            float imageHeight = imageView.getHeight();

            // Generate random x and y positions within the screen bounds, ensuring the image remains visible
            xOffset = random.nextFloat() * (maxX - imageWidth);
            yOffset = random.nextFloat() * (maxY - imageHeight);
        }

        public float getxOffset() {
            return xOffset;
        }

        public float getyOffset() {
            return yOffset;
        }

        public float getOriginalX() {
            return originalX;
        }

        public float getOriginalY() {
            return originalY;
        }

        public void nextImage() {
            imageId = imageId >= 5 ? 0 : imageId + 1; // Cycle through images
        }

        public String getCurrentImage() {
            return imageArray[imageId];
        }

        public void stopTimers() {
            if (offsetRunnable != null) timerHandler.removeCallbacks(offsetRunnable); // Stop position updates
        }
    }
}