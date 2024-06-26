package uk.youniverse.youniverse_voice.presentation.interfaces

import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import uk.youniverse.youniverse_voice.presentation.UPLOAD_ENDPOINT

interface ApiService {
    // Interface tells retrofit what API requests should be possible.
    @Multipart
    @POST(UPLOAD_ENDPOINT)
    fun UploadEntry(@Part filePart: MultipartBody.Part): Call<ResponseBody>
}