AndroidSensorLogger
===================

Logger for various sensors on Android, designed for use in physical activity recognition research.

The logger collects data from motion sensors on the phone.  At this point, it collects data from the accelerometer, the gryoscope, and then the virtual sensors of gravity, linear acceleration, and rotation vector.

Eventually feature selection will be added.  It was removed temporarily for performance reasons, but I plan on re-introducing it.  For now, my research must move forward so I have decided to implement feature extraction off-line.

The feature extraction uses the concept of sliding time windows.  Each time window has a specific length (e.g. 5 seconds) during which it records data.  The time windows overlap by a specified amount (e.g. 50%).  So time windows will often contain overlapping data.  This helps increase the accuracy of an activity recognition system during transitional periods between activities.  The time window is recorded as an instance in the dataset, to be used for training and classification.

Many features are extracted, such as standard statistical features (mean, standard deviation, etc.), regression lines, and some interesting new structural features (trend, magnitude of change, signed magnitude of change).

We will be using this to collect data for testing a new machine learning algorithm I have designed.  Hopefully in the future I will implement this algorithm on the phone for a fully-featured real-time activity recognition system.
