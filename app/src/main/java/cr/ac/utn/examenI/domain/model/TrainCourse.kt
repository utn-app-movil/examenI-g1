package cr.ac.utn.examenI.domain.model

enum class TrainCourse(val displayName: String) {
    KOTLIN("Kotlin"),
    ANDROID_STUDIO("Android Studio"),
    GIT("Git"),
    SCRUM("Scrum");

    override fun toString(): String = displayName
}