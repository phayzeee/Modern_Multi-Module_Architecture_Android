package app.phayzee.core_database.converter


import androidx.room.TypeConverter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

/**
 * Type converters for Room database.
 *
 * Room can only store primitive types by default.
 * These converters allow storing complex types like Lists, custom objects, etc.
 *
 * Example: Storing a List<String> as a JSON string in the database.
 */
class Converters {

    private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    /**
     * Converts a List<String> to JSON string for storage
     */
    @TypeConverter
    fun fromStringList(value: List<String>?): String? {
        if (value == null) return null
        val type = Types.newParameterizedType(List::class.java, String::class.java)
        val adapter = moshi.adapter<List<String>>(type)
        return adapter.toJson(value)
    }

    /**
     * Converts JSON string back to List<String>
     */
    @TypeConverter
    fun toStringList(value: String?): List<String>? {
        if (value == null) return null
        val type = Types.newParameterizedType(List::class.java, String::class.java)
        val adapter = moshi.adapter<List<String>>(type)
        return adapter.fromJson(value)
    }

    // Note: Room handles Long, Int, Double, String, Boolean primitives automatically
    // We only need converters for complex types like Lists, custom objects, etc.
}