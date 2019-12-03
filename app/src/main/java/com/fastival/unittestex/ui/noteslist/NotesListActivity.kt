package com.fastival.unittestex.ui.noteslist

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.fastival.unittestex.R
import com.fastival.unittestex.extension.observeOnce
import com.fastival.unittestex.models.Note
import com.fastival.unittestex.repository.NoteRepository
import com.fastival.unittestex.ui.Resource
import com.fastival.unittestex.ui.note.NoteActivity
import com.fastival.unittestex.ui.note.NoteViewModel
import com.fastival.unittestex.util.VerticalSpacingItemDecorator
import com.fastival.unittestex.viewmodels.ViewModelProviderFactory
import com.google.android.material.snackbar.Snackbar
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_notes_list.*
import javax.inject.Inject

class NotesListActivity : DaggerAppCompatActivity(), View.OnClickListener, NotesRecyclerAdapter.OnNoteListener {

    @Inject
    lateinit var providerFactory: ViewModelProviderFactory

    private val rvAdapter by lazy { NotesRecyclerAdapter(this) }
    private val viewModel by viewModels<NotesListViewModel> { providerFactory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notes_list)

        fab.setOnClickListener(this)

        viewModel
        Log.d("Main","NotesListActivity $viewModel")

        initRecyclerView()
    }

    override fun onStart() {
        super.onStart()
        subscribeObservers()
    }

    private fun subscribeObservers(){
        Log.d("Main", "subscribeObservers: called.")

        viewModel.notes.observe(this, Observer {
            it?.let {
                Log.d("Main", "subscribeObservers_viewModel.notes.observe")
                rvAdapter.setNote(it)
            }
        })

        viewModel.getNotes()
    }

    private fun initRecyclerView(){
        rvAdapter
        recyclerview.apply {
            layoutManager = LinearLayoutManager(this@NotesListActivity)
            addItemDecoration(VerticalSpacingItemDecorator(10))
            adapter = rvAdapter
            ItemTouchHelper(touchHelper).attachToRecyclerView(this)
        }
    }

    override fun onClick(p0: View) {
        when (p0.id) {
            R.id.fab -> {
                val intent = Intent(this, NoteActivity::class.java)
                startActivity(intent)
            }
        }
    }

    override fun onNoteClick(note: Note) {
        val intent = Intent(this, NoteActivity::class.java).apply {
            putExtra(getString(R.string.intent_note), note)
        }
        startActivity(intent)
    }

    private fun showSnackBar(message: String) {
        if (!TextUtils.isEmpty(message)) {

            Snackbar.make(nlParent, message, Snackbar.LENGTH_SHORT).show()
        }
    }

    val touchHelper: ItemTouchHelper.SimpleCallback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT or ItemTouchHelper.LEFT) {

        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ): Boolean {
            return false
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            val note = rvAdapter.getNote(viewHolder.adapterPosition)

            note?.let {
                rvAdapter.removeNote(it)

                try{
                    val deleteAction = viewModel.deleteNote(it)
                    deleteAction.observeOnce(this@NotesListActivity, Observer {resource ->
                        resource.msg?.let { showSnackBar(resource.msg) }
                    })
                } catch (e: Exception) {
                    e.printStackTrace()
                    Log.d("Main","${e.message}")
                    showSnackBar(e.message!!)
                }
            }

        }
    }
}
