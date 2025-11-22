package cr.ac.utn.movil.controllers

import android.content.Context
import cr.ac.utn.movil.data.MemoryDataManager
import cr.ac.utn.movil.entities.InsurancePolicy
import cr.ac.utn.movil.R

class InsurancePolicyController_ens(context: Context) {

    private val dataManager = MemoryDataManager
    private val ctx = context.applicationContext

    private val companies = listOf(
        ctx.getString(R.string.select_company_default),
        ctx.getString(R.string.company_ins),
        ctx.getString(R.string.company_mapfre),
        ctx.getString(R.string.company_magisterio),
        ctx.getString(R.string.company_panamerican),
        ctx.getString(R.string.company_assa),
        ctx.getString(R.string.company_qualitas),
        ctx.getString(R.string.company_sura)
    )

    private val coverageTypes = listOf(
        ctx.getString(R.string.select_type_default),
        ctx.getString(R.string.type_vehicle),
        ctx.getString(R.string.type_fire),
        ctx.getString(R.string.type_liability),
        ctx.getString(R.string.type_grouplife),
        ctx.getString(R.string.type_health),
        ctx.getString(R.string.type_surety),
        ctx.getString(R.string.type_marine),
        ctx.getString(R.string.type_engineering)
    )

    fun getCompaniesList() = companies
    fun getCoverageTypesList() = coverageTypes

    fun addPolicy(policy: InsurancePolicy): Result<String> {
        if (dataManager.getById(policy.policyNumber) != null)
            return Result.failure(Exception(ctx.getString(R.string.error_policy_exists)))
        if (!policy.expirationDate.after(policy.startDate))
            return Result.failure(Exception(ctx.getString(R.string.error_expiration_before_start)))
        if (policy.premium <= 0)
            return Result.failure(Exception(ctx.getString(R.string.error_invalid_premium)))

        dataManager.add(policy)
        return Result.success(ctx.getString(R.string.success_policy_saved))
    }

    fun updatePolicy(policy: InsurancePolicy): Result<String> {
        val existing = dataManager.getById(policy.policyNumber)
        if (existing != null && existing !== policy)
            return Result.failure(Exception(ctx.getString(R.string.error_policy_exists)))
        if (!policy.expirationDate.after(policy.startDate))
            return Result.failure(Exception(ctx.getString(R.string.error_expiration_before_start)))

        dataManager.update(policy)
        return Result.success(ctx.getString(R.string.success_policy_updated))
    }

    fun deletePolicy(policyNumber: String) = dataManager.remove(policyNumber)
    fun getAllPolicies() = dataManager.getAll().filterIsInstance<InsurancePolicy>()
    fun getPolicyByNumber(number: String) = dataManager.getById(number) as? InsurancePolicy
}