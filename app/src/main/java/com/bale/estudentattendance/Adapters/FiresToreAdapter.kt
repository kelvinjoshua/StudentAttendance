package com.bale.estudentattendance.Adapters

import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.*

abstract class FiresToreAdapter <VH : RecyclerView.ViewHolder>(private var query: Query) :
    RecyclerView.Adapter<VH>(),EventListener<QuerySnapshot> {
    private var registration: ListenerRegistration? = null

    private val snapshots = ArrayList<DocumentSnapshot>()
    open fun startListening() {
        if (registration == null) {
            registration = query.addSnapshotListener(this)
        }
    }

    open fun stopListening() {
        if (registration != null) {
            registration!!.remove()
            registration = null
        }


        snapshots.clear()
    }

    override fun onEvent(docs: QuerySnapshot?, error: FirebaseFirestoreException?) {
        if(error != null){
            Log.e("onEvent:error", error.toString())
        }
        for (change in docs!!.documentChanges) {
            when (change.type) {
                DocumentChange.Type.ADDED -> onDocumentAdded(change)
                DocumentChange.Type.MODIFIED -> onDocumentModified(change)
                DocumentChange.Type.REMOVED -> onDocumentRemoved(change)
            }
        }

    }

    protected open fun onDocumentAdded(change: DocumentChange?){
        snapshots.add(change!!.newIndex, change.document)
        notifyItemInserted(change.newIndex)
    }
    protected open fun onDocumentModified(change: DocumentChange?){
        if (change!!.oldIndex == change.newIndex) {
            snapshots[change.oldIndex] = change.document
            notifyItemChanged(change.oldIndex)
        } else {
            snapshots.removeAt(change.oldIndex)
            snapshots.add(change.newIndex, change.document)
            notifyItemMoved(change.oldIndex, change.newIndex)
        }
    }
    protected open fun onDocumentRemoved(change: DocumentChange?){
        snapshots.removeAt(change!!.oldIndex)
        notifyItemRemoved(change.oldIndex)
    }



    override fun getItemCount(): Int {
        return snapshots.size
    }

    protected fun getSnapshot(index: Int): DocumentSnapshot {
        return snapshots[index]
    }
    open fun onDataChanged() {}


    companion object {

        private const val TAG = "FirestoreAdapter"
    }
}