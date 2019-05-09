package com.example.opiniaodetudo.View

import android.app.Activity
import android.app.PendingIntent
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.ThumbnailUtils
import android.net.Uri
import android.os.AsyncTask
import android.os.Bundle
import android.provider.MediaStore
import android.support.v4.app.Fragment
import android.support.v4.app.NotificationCompat
import android.support.v4.app.NotificationManagerCompat
import android.support.v4.content.FileProvider
import android.support.v4.content.LocalBroadcastManager
import android.util.Log
import android.view.*
import android.widget.*
import com.example.opiniaodetudo.R
import com.example.opiniaodetudo.model.Review
import com.example.opiniaodetudo.model.ReviewRepository
import com.google.android.gms.common.util.IOUtils
import kotlinx.android.synthetic.main.sub_show_review.*
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

class FormFragment :Fragment() {

    private lateinit var mainView: View

    private var thumbnailBytes: ByteArray? = null

    companion object {
        const val TAKE_PICTURE_RESULT = 101
        const val NEW_REVIEW_MESSAGE_ID = 4584
        const val GEOCODER_FINALIZED_ACTION = "com.androiddesenv.opiniaodetudo.GEOCODER_FINALIZED"
    }
    private var file: File? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {

        mainView = inflater.inflate(R.layout.new_review_form_layout, null)

        val buttonSave = mainView.findViewById<Button>(R.id.bt_record)
        val textViewName = mainView.findViewById<EditText>(R.id.et_nameInfor)
        val textViewReview = mainView.findViewById<EditText>(R.id.et_op)

        val reviewToEdit = (activity!!.intent?.getSerializableExtra("item") as Review?)?.also { review ->
            textViewName.setText(review.name)
            textViewReview.setText(review.review)


            return mainView

        }


        buttonSave.setOnClickListener {
            val name = textViewName.text
            val review = textViewReview.text
            object : AsyncTask<Void, Void, Review>() {
                override fun doInBackground(vararg params: Void?): Review {
                    val repository = ReviewRepository(activity!!.applicationContext)
                    var entity: Review
                    if (reviewToEdit == null) {
                        entity = repository.save(
                            name.toString(),
                            review.toString(),
                            file?.toRelativeString(activity!!.filesDir),
                            thumbnailBytes
                        )
                    } else {
                        entity = repository.update(reviewToEdit.id, name.toString(), review.toString())
                    }
                    (activity as MainActivity).navigateTo(MainActivity.LIST_FRAGMENT)
                    return entity
                }
                override fun onPostExecute(result: Review) {
                    updateReviewLocation(result)
                    showReviewNotification(result)
                }
            }.execute()
            true
        }
        configurePhotoClick()
        handleImageShare()
        return mainView

    }

    private fun showReviewNotification(review: Review) {
        val deleteIntent = Intent(activity!!, MainActivity::class.java)
        deleteIntent.action = MainActivity.DELETE_NOTIFICATION_ACTION_NAME
        deleteIntent.putExtra(MainActivity.DELETE_NOTIFICATION_EXTRA_NAME, review.id)

        val deletePendingIntent: PendingIntent =
            PendingIntent.getActivity(
                activity!!,
                MainActivity.NEW_REVIEW_NOTIFICATION_MESSAGE_REQUEST,
                deleteIntent,
                PendingIntent.FLAG_UPDATE_CURRENT)

        val builder = NotificationCompat.Builder(
            activity!!,
            MainActivity.PUSH_NOTIFICATION_CHANNEL)
            .setSmallIcon(R.drawable.abc_ratingbar_small_material)
            .setContentTitle("Nova opinião no Opini")
            .setContentText(review.name)
        if(review.thumbnails != null){
            val thumbnail =
                BitmapFactory
                    .decodeByteArray(review.thumbnails, 0, review.thumbnails!!.size)
            val photo =
                BitmapFactory
                    .decodeFile(File(activity!!.filesDir, review.photoPath).absolutePath)
            builder.setLargeIcon(thumbnail)
            builder.setStyle(
                NotificationCompat.BigPictureStyle()
                    .bigPicture(photo)
                    .bigLargeIcon(null)
            )
        }else{
            builder.setStyle(
                NotificationCompat.BigTextStyle()
                    .setBigContentTitle(review.name)
                    .bigText(review.review)
            )
        }
        NotificationManagerCompat
            .from(activity!!).notify(NEW_REVIEW_MESSAGE_ID, builder.build())

        builder.addAction(0, "Apagar", deletePendingIntent)
    }

    private fun generateThumbnailBytes(thumbnail: Bitmap, targetSize: Int) {
        val thumbnailOutputStream = ByteArrayOutputStream()
        thumbnail.compress(Bitmap.CompressFormat.PNG, targetSize, thumbnailOutputStream)
        thumbnailBytes = thumbnailOutputStream.toByteArray()
    }

    private fun configurePhotoClick() {
        mainView.findViewById<ImageView>(R.id.iv_foto).setOnClickListener {
            val fileName = "${System.nanoTime()}.jpg"
            file = File(activity!!.filesDir, fileName)
            val uri = FileProvider.getUriForFile(activity!!,
                "com.androiddesenv.opiniaodetudo.fileprovider", file!!)
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            intent.putExtra(MediaStore.EXTRA_OUTPUT, uri)
            startActivityForResult(intent, TAKE_PICTURE_RESULT)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == TAKE_PICTURE_RESULT){
            if(resultCode == Activity.RESULT_OK){
                val photoView = mainView.findViewById<ImageView>(R.id.iv_foto)
                val bitmap = BitmapFactory.decodeStream(FileInputStream(file))
                val targetSize = 100
                val thumbnail = ThumbnailUtils.extractThumbnail(
                    bitmap,
                    targetSize,
                    targetSize
                )
                photoView.setImageBitmap(thumbnail)
                generateThumbnailBytes(thumbnail, targetSize)

            }else{
                Toast.makeText(activity, "Erro ao tirar a foto", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun updateReviewLocation(entity: Review) {
        Log.d("XPTO", (activity != null).toString())
        LocationService(activity!!).onLocationObtained{ lat,long ->
            val repository = ReviewRepository(activity!!.applicationContext)
            object: AsyncTask<Void, Void, Unit>() {
                override fun onPostExecute(result: Unit?) {
                    val intent = Intent(GEOCODER_FINALIZED_ACTION)
                    intent.putExtra("review", entity)
                    LocalBroadcastManager.getInstance(activity!!).sendBroadcast(intent)
                }
                override fun doInBackground(vararg params: Void?) {
                    repository.updateLocation(entity, lat, long)
                }
            }.execute()
        }
    }



    fun handleImageShare() {

        val intentParam = activity!!.intent
        Log.d("XPTO", "handleImageShare ${intentParam?.action}")

        if(intentParam?.action == Intent.ACTION_SEND) {

            Log.d("XPTO", "handleImageShare is send")
            intentParam?.extras.get(Intent.EXTRA_SUBJECT)?.let {
                mainView.findViewById<EditText>(R.id.tv_name).setText(it as String)
            }

            intentParam?.extras.get(Intent.EXTRA_TEXT)?.let {
                mainView.findViewById<EditText>(R.id.tv_op).setText(it as String)
            }

            intentParam?.extras.get(Intent.EXTRA_STREAM)?.let {

                Log.d("XPTO", "handleImageShare have photo")

                val fileName = "${System.nanoTime()}.jpg"
                file = File(activity!!.filesDir, fileName)
                IOUtils.copyStream(activity!!.contentResolver.openInputStream(it as Uri), FileOutputStream(file))
                    val photoView = mainView.findViewById<ImageView>(R.id.iv_foto)
                    val bitmap = BitmapFactory.decodeStream(FileInputStream(file))

                Log.d("XPTO", "handleImageShare photo read")

                val targetSize = 100
                val thumbnail = ThumbnailUtils.extractThumbnail(
                    bitmap,
                    targetSize,
                    targetSize
                )
                photoView.setImageBitmap(thumbnail)
                Log.d("XPTO", "handleImageShare thumbnail")
                generateThumbnailBytes(thumbnail, targetSize)
                Log.d("XPTO", "handleImageShare end")
            }
        } }

}