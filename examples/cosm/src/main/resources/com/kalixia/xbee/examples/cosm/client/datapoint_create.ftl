{
  "datapoints": [
    <#list datapoints as datapoint>
    {"at":"${datapoint.date}","value":"${datapoint.value}"}
    <#if datapoint_has_next>,</#if>
    </#list>
  ]
}