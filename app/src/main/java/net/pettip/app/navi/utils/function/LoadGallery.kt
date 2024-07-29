package net.pettip.app.navi.utils.function

import android.content.ContentUris
import android.content.Context
import android.graphics.Bitmap
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.provider.MediaStore
import android.util.Log

/**
 * @Project     : PetTip-Android
 * @FileName    : LoadGalleryImage
 * @Date        : 2024-05-02
 * @author      : CareBiz
 * @description : net.pettip.app.navi.utils.function
 * @see net.pettip.app.navi.utils.function.LoadGalleryImage
 */

object LoadGallery {

    /** gallery 에서 사진 uri 리스트를 받아오는 함수 */
    fun loadPhotos(context: Context): List<Uri> {
        val uriList = mutableListOf<Uri>()
        val projection = arrayOf(
            MediaStore.Images.Media._ID,
            MediaStore.Images.Media.DATA // 파일 경로를 가져오기 위해 추가
        )

        val selection = "${MediaStore.Images.Media.DATA} LIKE ?"
        val selectionArgs = arrayOf("%/Pictures/%")

        val sortOrder = "${MediaStore.Images.Media.DATE_ADDED} DESC"
        val cursor = context.contentResolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            projection,
            selection,
            selectionArgs,
            sortOrder
        )

        cursor?.use { c ->
            val idColumn = c.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
            while (c.moveToNext()) {
                val id = c.getLong(idColumn)
                val contentUri = ContentUris.withAppendedId(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    id
                )
                uriList.add(contentUri)
            }
        }
        return uriList
    }

    /** gallery 에서 동영상 uri 리스트를 받아오는 함수 */
    fun loadVideos(context: Context): List<Uri> {
        val uriList = mutableListOf<Uri>()
        val projection = arrayOf(MediaStore.Video.Media._ID)
        val cursor = context.contentResolver.query(
            MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
            projection,
            null,
            null,
            null
        )
        cursor?.use { c ->
            val idColumn = c.getColumnIndexOrThrow(MediaStore.Video.Media._ID)
            while (c.moveToNext()) {
                val id = c.getLong(idColumn)
                val contentUri = ContentUris.withAppendedId(
                    MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                    id
                )
                uriList.add(contentUri)
            }
        }
        return uriList
    }
}
