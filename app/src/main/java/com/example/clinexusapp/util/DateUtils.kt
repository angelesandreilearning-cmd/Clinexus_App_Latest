package com.example.clinexusapp.util

import java.text.SimpleDateFormat
import java.util.*

object DateUtils {
    fun formatChatTime(dateString: String?): String {
        if (dateString == null) return ""
        return try {
            val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
            val date = sdf.parse(dateString) ?: return ""
            
            val now = Calendar.getInstance()
            val chatDate = Calendar.getInstance()
            chatDate.time = date
            
            if (now.get(Calendar.YEAR) == chatDate.get(Calendar.YEAR) &&
                now.get(Calendar.DAY_OF_YEAR) == chatDate.get(Calendar.DAY_OF_YEAR)) {
                SimpleDateFormat("hh:mm a", Locale.getDefault()).format(date)
            } else {
                SimpleDateFormat("MMM dd", Locale.getDefault()).format(date)
            }
        } catch (e: Exception) {
            dateString
        }
    }
}
