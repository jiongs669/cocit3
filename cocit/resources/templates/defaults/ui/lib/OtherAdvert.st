<div id="media{$ui.id}"></div>
{import file="a.st" href=$data.href target=$data.target title=$data.title img=$data.img}
<script>
/*
$(document).ready(function(){
    $('#media{$ui.id}').flash(
    	{
            src: '{tostring bean=$data  prop="img"}'
            ,width: {$block.imageWidth}
            ,height: {$block.imageHeight}
        },{
            expressInstall: true
        }
    );
});
*/
</script>
