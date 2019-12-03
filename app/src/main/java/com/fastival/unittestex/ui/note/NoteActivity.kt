package com.fastival.unittestex.ui.note

import android.app.Activity
import android.os.Bundle
import android.text.Editable
import android.util.Log
import androidx.activity.viewModels
import com.fastival.unittestex.R
import com.fastival.unittestex.viewmodels.ViewModelProviderFactory
import dagger.android.support.DaggerAppCompatActivity
import java.lang.Exception
import javax.inject.Inject
import com.google.android.material.snackbar.Snackbar
import android.text.TextUtils
import android.text.TextWatcher
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import com.fastival.unittestex.extension.hideKeyboard
import com.fastival.unittestex.extension.toEditable
import com.fastival.unittestex.models.Note
import com.fastival.unittestex.ui.Resource
import com.fastival.unittestex.util.DateUtil
import kotlinx.android.synthetic.main.activity_note.*
import kotlinx.android.synthetic.main.layout_note_toolbar.*
import java.lang.NullPointerException


class NoteActivity : DaggerAppCompatActivity(), GestureDetector.OnGestureListener, GestureDetector.OnDoubleTapListener, View.OnTouchListener, View.OnClickListener, TextWatcher {

    @Inject
    lateinit var providerFactory: ViewModelProviderFactory

    private val viewModel by viewModels<NoteViewModel> { providerFactory }

    private lateinit var mDetector: GestureDetector

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_note)

        viewModel
        Log.d("Main", "noteActivity_viewModel: $viewModel")

        subscribeObservers()
        setListeners()

        if (savedInstanceState == null) {
            Log.d("Main","saveInstance == null")
            getIncomingIntent()
            enableEditMode()
        }

    }


    private fun subscribeObservers(){

        viewModel.note.observe(this, Observer {
            Log.d("Main", "noteObserve")
            showSnackBar("observeNote")
            setNoteProperties(it)
        })

        viewModel.viewState.observe(this, Observer {
            when(it) {
                NoteViewModel.ViewState.EDIT -> {
                    Log.d("Main", "ViewStateObserve_EDIT")
                    enableContentInteraction()
                }
                NoteViewModel.ViewState.VIEW -> {
                    Log.d("Main", "ViewStateObserve_VIEW")
                    disableContentInteraction()
                }
            }
        })
    }


    private fun setListeners(){
        mDetector = GestureDetector(this, this)
        note_text.setOnTouchListener(this)

        check_container.setOnClickListener(this)
        toolbar_check.setOnClickListener(this)

        note_text_title.setOnClickListener(this)

        back_arrow_container.setOnClickListener(this)
        toolbar_back_arrow.setOnClickListener(this)

        note_edit_title.addTextChangedListener(this)
        Log.d("Main", "setListeners()")
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean("has_started", true)
    }

    private fun getIncomingIntent(){
        try{
            val note: Note
            if (intent.hasExtra(getString(R.string.intent_note))) {
                note = Note(intent.getParcelableExtra(getString(R.string.intent_note)))
                viewModel.setIsNewNote(false)
            }

            else {
                note = Note(null,"Title", "sample", DateUtil.getCurrentTimeStamp())
                viewModel.setIsNewNote(true)
            }

            viewModel.setNote(note)

        } catch (e: Exception) {
            e.printStackTrace()
            showSnackBar(getString(R.string.error_intent_note))
        }
    }

    private fun setNoteProperties(note: Note) = try{
            note_text_title.text = note.title
            note_edit_title.text = note.title.toEditable()
            note_text.text = note.content.toEditable()
        } catch (e: NullPointerException) {
            e.printStackTrace()
            showSnackBar("Error displaying note properties")
        }


    private fun enableContentInteraction(){
        Log.d("Main", "enableContentInteraction()")

        back_arrow_container.visibility = View.GONE
        check_container.visibility = View.VISIBLE

        note_text_title.visibility = View.GONE
        note_edit_title.visibility = View.VISIBLE

        note_text.apply {
            keyListener = EditText(this@NoteActivity).keyListener
            isFocusable = true
            isFocusableInTouchMode = true
            isCursorVisible = true
            requestFocus()
        }
    }

    private fun disableContentInteraction(){
        Log.d("Main", "disableContentInteraction()")
        this.hideKeyboard()

        back_arrow_container.visibility = View.VISIBLE
        check_container.visibility = View.GONE

        note_text_title.visibility = View.VISIBLE
        note_edit_title.visibility = View.GONE

        note_text.apply {
            keyListener = null
            isFocusable = false
            isFocusableInTouchMode = false
            isCursorVisible = false
            clearFocus()
        }
    }


    private fun showSnackBar(message: String) {
        if (!TextUtils.isEmpty(message)) {

            Snackbar.make(noteParent, message, Snackbar.LENGTH_SHORT).show()
        }
    }

    override fun onShowPress(p0: MotionEvent?) {

    }

    override fun onSingleTapUp(p0: MotionEvent?): Boolean {
        return false
    }

    override fun onDown(p0: MotionEvent?): Boolean {
        return false
    }

    override fun onFling(p0: MotionEvent?, p1: MotionEvent?, p2: Float, p3: Float): Boolean {
        return false
    }

    override fun onScroll(p0: MotionEvent?, p1: MotionEvent?, p2: Float, p3: Float): Boolean {
        return false
    }

    override fun onLongPress(p0: MotionEvent?) {
    }

    override fun onDoubleTap(p0: MotionEvent?): Boolean {
        enableEditMode()
        return false
    }

    override fun onDoubleTapEvent(p0: MotionEvent?): Boolean {
        return false
    }

    override fun onSingleTapConfirmed(p0: MotionEvent?): Boolean {
        return false
    }

    override fun onTouch(p0: View?, event: MotionEvent?): Boolean {
        return mDetector.onTouchEvent(event)
    }

    override fun onClick(p0: View) {
        when(p0.id) {
            R.id.toolbar_back_arrow -> {
                Log.d("Main", "onClick_back_arrow")
                finish()
            }
            R.id.toolbar_check -> {
                Log.d("Main", "onClick_toolbar_check")
                disableEditMode()
            }
            R.id.note_text_title -> {
                Log.d("Main", "onClick_note_text_title")
                enableEditMode()
                note_edit_title.requestFocus()
                note_edit_title.setSelection(note_edit_title.length())
            }
        }
    }

    private fun enableEditMode() {
        viewModel.setViewState(NoteViewModel.ViewState.EDIT)
    }

    private fun disableEditMode(){
        viewModel.setViewState(NoteViewModel.ViewState.VIEW)

        if (!TextUtils.isEmpty(note_text.text)) {
            try {
                viewModel.updateNote(note_edit_title.text.toString(), note_text.text.toString())
            } catch (e: Exception) {
                e.printStackTrace()
                showSnackBar("Error setting note properties")
            }
        }

        saveNote()
    }

    private fun saveNote(){
        try{
            viewModel.saveNote().observe(this, Observer {
                when(it) {
                    is Resource.Success -> {
                        Log.d("Main", "onChanged: save note: success..")
                        showSnackBar(it.msg!!) }
                    is Resource.Error -> {
                        Log.d("Main", "onChanged: save note: error..")
                        showSnackBar(it.msg!!) }
                    is Resource.Loading -> {
                        Log.d("Main", "onChanged: save note: loading..")
                    }
                }
            })
        }catch (e: Exception) {
            e.printStackTrace()
            showSnackBar(e.message.toString())
        }
    }

    override fun afterTextChanged(p0: Editable?) {
    }

    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
    }

    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        note_text_title.text = p0.toString()
    }

    override fun onBackPressed() =
        if (viewModel.shouldNavigateBack()) {
            super.onBackPressed()
        } else {
            onClick(check_container)
        }

}
