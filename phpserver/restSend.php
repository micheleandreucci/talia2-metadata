<?php
const restHost="http://localhost:9100";

ini_set('memory_limit', '1024M'); // or you could use 1G
ini_set('max_execution_time', 3600); //3600 seconds = 1 hour
if(isset($_POST['inner'])){
  $collname=$_POST['collname'];
  if($collname=="new"){
    $collname=$_POST['newcollection'];
    if(createCollection($collname)=="ERROR"){
      exit("Error creating collection");
    }
  }
  $checkboxes=$_POST['index'];
  if($checkboxes) {
    if(is_array($checkboxes)) {
      $file_names=array();
      foreach($checkboxes as $index){
        global $url;
        $name=$index;
        $name=truncate_extension(substr($name,strrpos($name,"/")+1));
        $delid=getDeliverableID($name);
        echo("<br>");
        $url = restHost."/ivp/v2/addDocument";
        $response=sendRequest($index,$collname,"PUT",$delid);
        if($response=="Alcuni documenti nella collezione possiedono già lo stesso nome di alcuni documenti che si sta tentando di caricare."){
          $url = restHost."/ivp/v2/updateDocument";
          echo("<br>");
          sendRequest($index,$collname,"POST",$delid);
          echo("<br>");
        }
        $nlp_response=NLP($collname,$name);
      }
      $phrase_response=phrase($collname);
      $analyze_response=analyze($collname);
      $SE_response=createSE($collname);
      $RI_response=createRI($collname);
    }
  }else{
    echo("no file selected");
  }

}

/*
  Richiamare NLP(coll,doc) //ogni document
  Richiamare phrase extractor GET phrase/{collection}/w2p/{threshold}/{mincount}
  GET analyze/{collection}/{language}
  PUT createSE/{collection}
  PUT createRI

*/

function NLP($collname,$filename){
  $url=restHost."/ivp/v2/nlp/".$collname."/".$filename;
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

function analyze($collname){
  $url=restHost."/ivp/v2/analyze/".$collname."/en";
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

function phrase($collname){
  $url=restHost."/ivp/v2/phrase/".$collname."/w2p/30/4";
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

function createSE($collname){
  $url=restHost."/ivp/v2/createSE/".$collname;
  $curl = curl_init();
  curl_setopt_array($curl, array(
  CURLOPT_URL => $url,
  CURLOPT_RETURNTRANSFER => 1,
  CURLOPT_MAXREDIRS => 10,
  CURLOPT_TIMEOUT => 30,
  //CURLOPT_HTTP_VERSION => CURL_HTTP_VERSION_1_1,
  CURLOPT_CUSTOMREQUEST => "PUT",
  CURLOPT_HTTPHEADER => array('Content-Type: text/plain'),
));

  $response = curl_exec($curl);
  $info = curl_getinfo($curl);
  $err = curl_error($curl);
  curl_close($curl);
  return $response;
}

function createRI($collname){
  $vecdim=500;
  $seed=1;
  $vocsize=100000;
  $url=restHost."/ivp/v2/createRI/".$collname."/".$vecdim."/".$seed."/".$vocsize;
  $curl = curl_init();
  curl_setopt_array($curl, array(
  CURLOPT_URL => $url,
  CURLOPT_RETURNTRANSFER => 1,
  CURLOPT_MAXREDIRS => 10,
  CURLOPT_TIMEOUT => 30,
  //CURLOPT_HTTP_VERSION => CURL_HTTP_VERSION_1_1,
  CURLOPT_CUSTOMREQUEST => "PUT",
  CURLOPT_HTTPHEADER => array('Content-Type: text/plain'),
));

  $response = curl_exec($curl);
  $info = curl_getinfo($curl);
  $err = curl_error($curl);
  curl_close($curl);
  return $response;
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

function createCollection($collname){
  // curl
  $curl = curl_init();
  $url = restHost."/ivp/v2/createCollection";;
  $url=$url."/en/".$collname;
  curl_setopt_array($curl, array(
    CURLOPT_URL => $url,
    CURLOPT_RETURNTRANSFER => 1,
    CURLOPT_MAXREDIRS => 10,
    CURLOPT_TIMEOUT => 30,
    //CURLOPT_HTTP_VERSION => CURL_HTTP_VERSION_1_1,
    CURLOPT_CUSTOMREQUEST => "PUT",
    CURLOPT_POST => 1,
  ));

  $response = curl_exec($curl);

  $info = curl_getinfo($curl,CURLINFO_HTTP_CODE);
  $err = curl_error($curl);
  curl_close($curl);
  if($info!=200){
    return "ERROR";
  }
  return $response;
}

// files to upload
function sendRequest($filename,$collname,$method,$delid){
  // data fields for POST request
  $fields = array("filterspart"=>$_POST['filterspart'], "collname"=>$collname, "prefixpart"=>$_POST['prefixpart'],"sqlid"=>$delid);

  $files[$filename] = file_get_contents($filename);

  // curl
  $curl = curl_init();

  $url_data = http_build_query($fields);

  $boundary = uniqid();
  $delimiter = '-------------' . $boundary;

  $post_data = build_data_files($boundary, $fields, $files);

  global $url;
  curl_setopt_array($curl, array(
    CURLOPT_URL => $url,
    CURLOPT_RETURNTRANSFER => 1,
    CURLOPT_MAXREDIRS => 10,
    CURLOPT_TIMEOUT => 30,
    //CURLOPT_HTTP_VERSION => CURL_HTTP_VERSION_1_1,
    CURLOPT_CUSTOMREQUEST => $method,
    CURLOPT_POST => 1,
    CURLOPT_POSTFIELDS => $post_data,
    CURLOPT_HTTPHEADER => array(
      //"Authorization: Bearer $TOKEN",
      "Content-Type: multipart/form-data; boundary=" . $delimiter,
      "Content-Length: " . strlen($post_data)
    ),

  ));

  $response = curl_exec($curl);

  $info = curl_getinfo($curl);

  var_dump($response);
  $err = curl_error($curl);
  var_dump($err);
  curl_close($curl);
  return $response;
}

function build_data_files($boundary, $fields, $files){
    $data = '';
    $eol = "\r\n";
    $delimiter = '-------------' . $boundary;
    foreach ($files as $name => $content) {
      $name=substr($name,strrpos($name,"/")+1);
      $name=truncate_extension($name);
        $data .= "--" . $delimiter . $eol
            . 'Content-Disposition: form-data; name="' . "filepart" . '"; filename="' . $name . '"' . $eol
            //. 'Content-Type: image/png'.$eol
            . 'Content-Transfer-Encoding: binary'.$eol
            ;

        $data .= $eol;
        $data .= $content . $eol;
    }
    $data .= "--" . $delimiter . $eol
        . 'Content-Disposition: form-data; name="' . "namepart" . "\"".$eol.$eol
        . $name . $eol;

    foreach ($fields as $name => $content) {
        $data .= "--" . $delimiter . $eol
            . 'Content-Disposition: form-data; name="' . $name . "\"".$eol.$eol
            . $content . $eol;
    }
    $data .= "--" . $delimiter . "--".$eol;
    return $data;
}

function truncate_extension($string){
  $exposition=strrpos($string,".");
  $length=strlen($string);
  $exlen=strlen(substr($string,$exposition));
   return(substr($string,0,$length-$exlen));
}
?>
