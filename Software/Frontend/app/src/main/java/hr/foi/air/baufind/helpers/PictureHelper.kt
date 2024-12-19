package hr.foi.air.baufind.helpers

class PictureHelper{
    companion object{
        fun decodeBase64ToByteArray(base64: String?): ByteArray? {
            return base64?.let {
                try {
                    android.util.Base64.decode(it, android.util.Base64.DEFAULT)
                } catch (e: IllegalArgumentException) {
                    null
                }
            }
        }
    }
}