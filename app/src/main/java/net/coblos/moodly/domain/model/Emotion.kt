package net.coblos.moodly.domain.model

enum class Emotion(val label: String, val emoji: String, val colorHex: String, val score: Int) {
    HAPPY("Happy", "😊", "#FFD700", 5),
    CALM("Calm", "😌", "#87CEFA", 4),
    SAD("Sad", "😢", "#4682B4", 2),
    ANGRY("Angry", "😡", "#FF4500", 1),
    ANXIOUS("Anxious", "😟", "#9370DB", 3)
}
