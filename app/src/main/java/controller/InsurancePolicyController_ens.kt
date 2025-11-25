package cr.ac.utn.movil.controllers

import android.content.Context
import cr.ac.utn.movil.R
import cr.ac.utn.movil.data.MemoryDataManager
import cr.ac.utn.movil.entities.InsurancePolicy

class InsurancePolicyController_ens(private val context_ens: Context) {

    private val dataManager_ens = MemoryDataManager
    private val ctx_ens = context_ens.applicationContext

    fun getCompaniesList_ens() = listOf(
        ctx_ens.getString(R.string.select_company_default_ens),
        ctx_ens.getString(R.string.company_ins_ens),
        ctx_ens.getString(R.string.company_mapfre_ens),
        ctx_ens.getString(R.string.company_magisterio_ens),
        ctx_ens.getString(R.string.company_panamerican_ens),
        ctx_ens.getString(R.string.company_assa_ens),
        ctx_ens.getString(R.string.company_qualitas_ens),
        ctx_ens.getString(R.string.company_sura_ens)
    )

    fun getCoverageTypesList_ens() = listOf(
        ctx_ens.getString(R.string.select_type_default_ens),
        ctx_ens.getString(R.string.type_vehicle_ens),
        ctx_ens.getString(R.string.type_fire_ens),
        ctx_ens.getString(R.string.type_liability_ens),
        ctx_ens.getString(R.string.type_grouplife_ens),
        ctx_ens.getString(R.string.type_health_ens),
        ctx_ens.getString(R.string.type_surety_ens),
        ctx_ens.getString(R.string.type_marine_ens),
        ctx_ens.getString(R.string.type_engineering_ens)
    )

    fun addPolicy_ens(policy: InsurancePolicy): Result<String> {
        if (dataManager_ens.getById(policy.policyNumber_ens) != null)
            return Result.failure(Exception(ctx_ens.getString(R.string.error_policy_exists_ens)))

        if (!policy.expirationDate_ens.after(policy.startDate_ens))
            return Result.failure(Exception(ctx_ens.getString(R.string.error_expiration_before_start_ens)))

        if (policy.premium_ens <= 0)
            return Result.failure(Exception(ctx_ens.getString(R.string.error_invalid_premium_ens)))

        dataManager_ens.add(policy)
        return Result.success(ctx_ens.getString(R.string.success_policy_saved_ens))
    }

    fun updatePolicy_ens(policy: InsurancePolicy): Result<String> {
        val existing = dataManager_ens.getById(policy.policyNumber_ens)
            ?: return Result.failure(Exception("Policy not found"))

        if (!policy.expirationDate_ens.after(policy.startDate_ens))
            return Result.failure(Exception(ctx_ens.getString(R.string.error_expiration_before_start_ens)))

        dataManager_ens.update(policy)
        return Result.success(ctx_ens.getString(R.string.success_policy_updated_ens))
    }

    fun deletePolicy_ens(policyNumber: String) =
        dataManager_ens.remove(policyNumber)

    fun getAllPolicies_ens() =
        dataManager_ens.getAll().filterIsInstance<InsurancePolicy>()

    fun getPolicyByNumber_ens(number: String) =
        dataManager_ens.getById(number) as? InsurancePolicy
}