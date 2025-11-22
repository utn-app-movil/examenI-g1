package cr.ac.utn.movil.controllers

import cr.ac.utn.movil.identities.mark_Campaign
import cr.ac.utn.movil.data.MemoryDataManager

class mark_CampaignController {


    private val dataManager = MemoryDataManager

    fun processCampaign(campaign: mark_Campaign): Boolean {
        // 1. Validation Logic
        if (!validateBudget(campaign.Budget)) return false
        if (!validateChannel(campaign.Channel)) return false
        if (!validateDates(campaign.StartDate, campaign.EndDate)) return false

        // 2. Persistence using MemoryDataManager
        return storeCampaign(campaign)
    }

    private fun validateBudget(budget: Double): Boolean {

        return budget > 1.0
    }


    private fun validateChannel(channel: String): Boolean {

        val allowedChannels = listOf("Digital", "TV", "Radio")
        return allowedChannels.any { it.equals(channel, ignoreCase = true) }
    }


    private fun validateDates(startDate: Long, endDate: Long): Boolean {

        return startDate <= endDate
    }

    private fun storeCampaign(campaign: mark_Campaign): Boolean {
        return try {

            dataManager.add(campaign)
            true
        } catch (e: Exception) {

            println("Error storing campaign with ID: ${campaign.ID}. Error: ${e.message}")
            false
        }
    }

    fun getAllCampaigns(): List<mark_Campaign> {

        return dataManager.getAll().filterIsInstance<mark_Campaign>()
    }
}