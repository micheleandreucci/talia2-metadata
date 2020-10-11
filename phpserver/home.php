<html>
<head>  <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css" integrity="sha384-Gn5384xqQ1aoWXA+058RXPxPg6fy4IWvTNh0E263XmFcJlSAwiGgFAW/dAiS6JXm" crossorigin="anonymous"></head>
<style>

</style>
<body>
  <?php
    //VARIABLES
    $searchDir=".";
    $url="http://localhost:8000";
    //__________________________

    echo("<form method='post' action=".$url."/selectFiles.php>");
    ?>
    <table class='table'>
      <thead class='thead-dark'>
        <th scope='col'>
          <div class='form-row'>
            <div class='form-group col-md-4'>
              <label for='dir'>Path</label>
              <select id='inputFilter' name='dir' class='form-control'>
                <?php
                $cdir = array_diff( scandir($searchDir), array("..") );
                $cont=0;
                foreach($cdir as $result){
                  $result=$searchDir."/".$result;
                  if(is_dir($result)||is_array($result)){
                    $dirs[$cont]=$result;
                    echo "<option value='$dirs[$cont]'>".substr($dirs[$cont],strlen($searchDir)+1)."</option>";
                    $cont++;
                  }
                }
                ?>
              </select>
            </div>
            <div class='form-group col-md-3'>
              <label for='submit' style='opacity:0'>submit</label>
              <input type="submit" class='form-control btn-primary' value="Submit" name="submit"></th>
            </div>
          </th>
        </div>
      </table>
    </form>
  </body>
