package com.example.demonotesapp

object DataObject {
    var listdata = mutableListOf<NoteInfo>()

    fun setData(noteId: String, title: String, description:String, spinner: String) {
        listdata.add(NoteInfo(noteId, title, description, spinner))
    }

    fun getAllData(): List<NoteInfo> {
        return listdata
    }

    fun deleteAll(){
        listdata.clear()
    }

    fun getData(id: String): NoteInfo? {
        return listdata.find { it.id == id }
    }

    fun deleteData(id: String){
        val model = listdata.find { it.id == id }
        listdata.remove(model)
    }

    fun updateData(id: String, title: String, description: String, spinner: String)
    {
        listdata.find { it.id == id }?.apply {
            this.title= title
            this.description= description
            this.spinner= spinner
        }
    }
}