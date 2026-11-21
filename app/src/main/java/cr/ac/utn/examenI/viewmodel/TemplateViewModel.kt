package cr.ac.utn.examenI.viewmodel

import androidx.lifecycle.ViewModel
import cr.ac.utn.examenI.data.memory.MemoryDataManager
import cr.ac.utn.examenI.domain.model.TemplateModel
import cr.ac.utn.examenI.domain.repository.IRepository

class TemplateViewModel : ViewModel() {
    companion object {
        private val repository: IRepository = MemoryDataManager()

        fun getAll(): List<TemplateModel> = repository.getAll().filterIsInstance<TemplateModel>()
        fun getById(id: String): TemplateModel? = repository.getById(id) as? TemplateModel
        fun save(item: TemplateModel) = repository.add(item)
        fun update(item: TemplateModel) = repository.update(item)
        fun delete(id: String) = repository.delete(id)
    }
}
