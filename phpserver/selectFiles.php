<html>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css" integrity="sha384-Gn5384xqQ1aoWXA+058RXPxPg6fy4IWvTNh0E263XmFcJlSAwiGgFAW/dAiS6JXm" crossorigin="anonymous">
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/js/bootstrap.min.js"></script>

  <style>
    input[type=checkbox]{
      zoom:1.5;
      margin-left:50%;
    }
    #main{
      zoom:1.5;
      margin-left:7%;
    }
  </style>
</head>

<script>
var collezioni=Array();
var checkboxes=[];
</script>
<?php
header('Content-type: text/html; charset=utf-8');
const restHost="http://localhost:9100";
const phpHost="http://localhost:8000";


function createCollection($collectionName){
  // curl
  $curl = curl_init();
  $url = restHost."/ivp/v2/createCollection/en/{$collectionName}";
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
  $info = curl_getinfo($curl);
  $err = curl_error($curl);
  curl_close($curl);
  return $response;
}


//request collections' name from rest
function requestCollections(){
  // curl
  $curl = curl_init();
  $url= restHost."/ivp/v2/collections";
  //set curl
  curl_setopt_array($curl, array(
    CURLOPT_URL => $url,
    CURLOPT_RETURNTRANSFER => 1,
    CURLOPT_MAXREDIRS => 10,
    CURLOPT_TIMEOUT => 30,
    //CURLOPT_HTTP_VERSION => CURL_HTTP_VERSION_1_1,
    CURLOPT_CUSTOMREQUEST => "GET",
    )
);
  //send request
  $response = curl_exec($curl);
  $info = curl_getinfo($curl);
  $err = curl_error($curl);
  return $response;
}

function dirToArray($dir) {
  $result = array();
  $cdir = scandir($dir);
  foreach ($cdir as $key => $value) {
    if (!in_array($value,array(".",".."))) {
      if (is_dir($dir . DIRECTORY_SEPARATOR . $value)) {
        $result[$value] = dirToArray($dir . DIRECTORY_SEPARATOR . $value);
      } else {
        $result[] = $value;
      }
    }
  }
  return $result;
}

function explore($string,&$collections){
  $ncoll=0;
  foreach($string as $collections[$ncoll] => $folders[$ncoll]){
    $ncoll++;
  }
  $col_files=array();
  if($ncoll>0){
    for($i=0;$i<count($collections);$i++) {
      if(is_array($folders[$i])){
        foreach($folders[$i] as $key =>$y) {
          $index=0;
          explore_content($y,$index,$col_files[$i],$key);
        }
      }
    }
  }
  return $col_files;
}

function explore_content($content, &$i, &$folcont,$dir) {
  if(is_array($content)||is_object($content)){
    foreach($content as $key => $single_folcont){
      if(is_array($single_folcont)){
        foreach($single_folcont as $inner_content){
          if(!is_array($inner_content)){
            $folcont[$i] = $dir.'/'.$key.'/'.$inner_content;
            $i++;
          }
        }
      } else {
        $folcont[$i] = $dir.'/'.$single_folcont;
        $i++;
      }
    }
  }
}

if(isset($_POST['submit'])){
  $dir=$_POST['dir'];
  $arr = (dirToArray($dir));
  $contenuto=explore($arr,$collections);
  $remoteCollections=requestCollections();
?>
  <table class='table'>
  <?php echo("<form name='Form1' method='post' action=".phpHost."/selectFiles.php'>");?>
      <thead class='thead-dark'>
        <th scope='col'>
          <div class='form-row'>
            <div class='form-group col-md-4'>
              <label for='inputFilter'>Filters</label>
              <select id='inputFilter' name='filters' class='form-control'>
                <option selected value='All'>All</option>
                  <?php
                  $filter=$_POST['filters'];
                  for($i=0;$i<count($collections);$i++){
                    echo "<script>collezioni.push(".'"'.$collections[$i].'"'.");</script>";
                    echo "<option value='$collections[$i]'>$collections[$i]</option>";
                  }
                  ?>
              </select>
            </div>

            <div class='form-group col-md-3'>
              <label for='sendFile' style='opacity:0'>files</label>
              <button type='button' class='form-control btn-success' data-toggle='modal' data-target='#myModal'>Send</button>
              <div class='modal fade' id='myModal' role='dialog'>
                <div class='modal-dialog'>
                  <div class='modal-content'>
                    <div class='modal-header'>
                      <button type='button' class='close' data-dismiss='modal'>&times;</button>
                    </div>
                    <div class='modal-body'>
                      <p class='text-dark'>Collection name</br></p>
                      <div class="form-group row">
                        <div class="col-sm-5">
                          <select id='collname' name='collname' class='form-control'>
                            <option value="new">new</option>
                            <?php
                            $array=json_decode($remoteCollections);
                            foreach($array as $namecol){
                              echo("<option value='".substr($namecol,2)."'>".substr($namecol,2)."</option>");
                            }
                            ?>
                          </select>
                        </div>
                        <div class="col-sm-5">
                          <input type="text" class="form-control" name="newcollection" id="newcollection" style="display: inline;" placeholder="New collection name">
                        </div>
                      </div>
                      <p class='text-dark'>Filters</br>
                      <div class="form-group row">
                        <div class="col-sm-10">
                          <input type="text" class="form-control" name="filterspart" placeholder="Specify filters">
                        </div>
                      </div>
                      </p>
                      <p class='text-dark'>Prefix</br>
                      <div class="form-group row">
                        <div class="col-sm-10">
                          <input type="text" class="form-control" name="prefixpart" placeholder="Specify prefix">
                        </div>
                      </div>
                      </p>

                     </div>

                      <div class='modal-footer'>
                        <input type='submit' class='btn btn-primary mr-auto' value='Add' name='inner' onClick='changeAction()'/>

                        <button type='button'class='btn btn-danger' data-dismiss='modal'>Close</button>
                      </div>
                    </div>
                  </div>
                </div>
              </div>
            </div>
            <table class='table table-sm' border='1'>
              <thead class='thead-dark'>
                <th scope='col' id="toggle">Toggle <input type='checkbox' id='main' name='index[]' onClick='toggle(this,filters.value)' checked/><span id="nChecked"></span><br/></th>
                <th scope='col'>File name</th>
<?php
    if(is_array($collections) && is_array($contenuto)){
      $contentsize=sizeof($contenuto);
      $collectionsize=count($collections);
      for($i=0;$i<$collectionsize && $i<$contentsize;$i++){
          if(isset($contenuto[$i]) && (is_array($contenuto[$i])||is_object($contenuto[$i]))){
            $j=0;
            foreach ($contenuto[$i] as $value) {
              $value=utf8_encode($value);
          //  if( preg_match("/.pdf$/",$value)){  N.B. Uncomment this to filter only pdf files
                echo "<tr class='$collections[$i]'><td><p><input type='checkbox' class='$collections[$i]' name='index[]' value='./".$dir.'/'.$collections[$i]."/".utf8_encode($value)."'checked></p></td>";
                echo "<td><a target='_blank' rel='noopener noreferrer' href='./".$dir.'/'.$collections[$i]."/$value'>".substr($value,strrpos($value,'/')+1)."</td> </tr>";
                $j++;
              }
              ?>
              <script language="JavaScript">var collection="";$('#inputFilter option[value="<?php echo($collections[$i]); ?>"]').text('<?php echo($collections[$i])."        (".$j.")"; ?>');</script>
              <?php
          //  }
          }
        }
      }
    echo "</form></th>";
  }

?>
<script language="JavaScript">

$('[id="inputFilter"]').on('change', function() {
  string=this.value;
  if(string=="All"){
    document.getElementById("inputFilter").selectedIndex=0;
    for(i=0;i<collezioni.length;i++){
      selector=collezioni[i].replace(/ /gm,'.');
      selector="."+selector;
      $(selector).show();
    }
  }else{
    for(i=0;i<collezioni.length;i++){
      selector=collezioni[i].replace(/ /gm,'.');
      selector="."+selector;
      if(collezioni[i]==string){
        $(selector).show();
      }else{
        $(selector).hide();
      }
    }
  }
  })

$('[name="collname"]').on('change', function() {
    if ($(this).val()=='new') {
      document.getElementById('newcollection').style["display"]="inline";
        document.getElementById('newcollection').setAttribute("required","required");
    }else{
      document.getElementById('newcollection').style["display"]="none";
      document.getElementById('newcollection').removeAttribute("required");
    }
  })

  $('[name="index[]"]').on('change',function(){
    var num=$('input[name="index[]"]:checked').length;
    if($('#main').is(':checked')){
      num=num-1;
    }
    document.getElementById("nChecked").innerHTML=(num);
  })


function toggle(source,filter) {
  checkboxes = document.getElementsByName('index[]');
  if(filter=='All'){
    for(var i=0, n=checkboxes.length;i<n;i++) {
      checkboxes[i].checked = source.checked;
    }
  }else{
    for(var i=0, n=checkboxes.length;i<n;i++) {
      if(checkboxes[i].className==filter){
        checkboxes[i].checked = source.checked;
      }
    }

  }
}

function changeAction()
{
    document.Form1.action = "<?php echo(phpHost."/restSend.php")?>";    // target
    document.Form1.submit();        // Submit the page
    return true;
}

function filter(string){

  if(string=="All"){
    document.getElementById("inputFilter").selectedIndex=0;
    for(i=0;i<collezioni.length;i++){
      selector=collezioni[i].replace(/ /gm,'.');
      selector="."+selector;
      $(selector).show();
    }
  }else{
    for(i=0;i<collezioni.length;i++){
      selector=collezioni[i].replace(/ /gm,'.');
      selector="."+selector;
      if(collezioni[i]==string){
        $(selector).show();
      }else{
        $(selector).hide();
      }
    }
  }
}
</script>
