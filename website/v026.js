// v0.33 bootstrap: load the verified v0.32 clinical tools first, then the new folder hierarchy.
(function(){
  const core=document.createElement('script');
  core.src='v026-core.js';
  core.onload=()=>{
    const hierarchy=document.createElement('script');
    hierarchy.src='v033.js';
    document.body.appendChild(hierarchy);
  };
  document.body.appendChild(core);
})();
