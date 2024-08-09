AnimatrX brings your Android app to life with stunning image animations! Dive into a world of dynamic visuals where images rotate, scale, and teleport across the screen, creating a captivating and interactive user experience.

🌟 Key Features

Dynamic Image Animation: Watch as images glide to random positions around the edges of the screen, complete with smooth rotation and scaling effects.
Seamless Image Transitions: Experience eye-catching transitions with zoom-out and zoom-in effects, making each image change feel magical.
Interactive Button: Tap the button to cycle through images, letting users engage directly with the app and enjoy fresh visuals.

🚀 How It Works

Setup and Initialization: At launch, the app sets up the AnimationManager based on the screen dimensions, readying it for action.
Continuous Animation Loop: Using a Handler, the app continuously triggers the animateImage method, ensuring a steady flow of animations.
Image Cycling: The AnimationManager cycles through a curated set of images, updating the ImageView with each transition.
Zoom Effects: Experience smooth zoom-out and zoom-in effects that make each image transition truly engaging.

🔧 Code Breakdown

MainActivity: Orchestrates the ImageView and button interactions, managing and coordinating all animations.
AnimationManager: Takes charge of image transitions, handling random positioning within the screen bounds to keep things lively.
Animation Details: Employs ViewPropertyAnimator for fluid animations and Handler for timely updates.
