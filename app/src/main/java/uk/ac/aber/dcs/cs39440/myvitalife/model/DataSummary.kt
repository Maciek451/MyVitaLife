package uk.ac.aber.dcs.cs39440.myvitalife.model

/**
 * A data class representing stats items for data summary
 *
 * @param firstStat The first statistic in the summary.
 * @param secondStat The second statistic in the summary.
 */
data class DataSummary(
    var firstStat: String = "",
    var secondStat: String = "",
)
