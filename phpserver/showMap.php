<?php
const restHost="http://localhost:9100";
  $result=$_POST['deliverables'];
  $searchFor=$_POST['info'];
  $nresult=10;
  $resultList=array();
  if($result){
    $cont=0;
    foreach($result as $value){
      if($cont >= $nresult){
        break;
      }
      if ($searchFor=="partners") {
        $service="getProjectPartners";
      } else if ($searchFor=="stakeholders") {
        $service="getProjectStakeholders";
      } else {
        $service="exportMapData";
      }
      $delId=getDeliverableID($value);

      $response=getMetadata($service,$delId);
      if(!in_array($response, $resultList, true)){
        echo("<div style='border-style:solid; border-color:#DDDEE; border-width:1px;'>");
        echo("<h1>".$value." data"."</h1>");
        array_push($resultList, $response);
        $partners_array = array();
        $fields_array = array();
        $partners_array = explode("---", $response);
        foreach($partners_array as $partner) {
            $fields_array = explode("|", $partner);
            echo("{\"partner_name\":\"".$fields_array[0]."\",\"lead_partner\":\"".$fields_array[1]."\",\"partner_country\":\"".$fields_array[2]."\",\"partner_amount\":".$fields_array[3]."}");
        }
        $cont++;
        echo("<br><br><br>");
      }
      echo("</div>");
    }
  }

  function getDeliverableID($filename){
    // data fields for POST request
    // curl
    $url=restHost."/ivp/v2/getDeliverableID/".$filename;
    $curl = curl_init();
    curl_setopt_array($curl, array(
    CURLOPT_URL => $url,
    CURLOPT_RETURNTRANSFER => 1,
    CURLOPT_MAXREDIRS => 10,
    CURLOPT_TIMEOUT => 30,
    //CURLOPT_HTTP_VERSION => CURL_HTTP_VERSION_1_1,
    CURLOPT_CUSTOMREQUEST => "GET",
    CURLOPT_HTTPHEADER => array('Content-Type: text/plain'),
  ));

    $response = curl_exec($curl);
    $info = curl_getinfo($curl);
    $err = curl_error($curl);
    curl_close($curl);
    return $response;
  }

  function getMetadata($service,$sqlId){//exportMapData
    // data fields for POST request
    // curl
    $url=restHost."/ivp/v2/".$service."/".$sqlId;
    $curl = curl_init();
    curl_setopt_array($curl, array(
    CURLOPT_URL => $url,
    CURLOPT_RETURNTRANSFER => 1,
    CURLOPT_MAXREDIRS => 10,
    CURLOPT_TIMEOUT => 30,
    //CURLOPT_HTTP_VERSION => CURL_HTTP_VERSION_1_1,
    CURLOPT_CUSTOMREQUEST => "GET",
    CURLOPT_HTTPHEADER => array('Content-Type: text/plain'),
  ));

    $response = curl_exec($curl);
    $info = curl_getinfo($curl);
    $err = curl_error($curl);
    curl_close($curl);
    return $response;
  }

?>
