// v0.33 bootstrap. The verified calculators remain inline as a safe fallback;
// v033.js is loaded after the page has initialized.
(function(){
  const hierarchy=document.createElement('script');
  hierarchy.src='v033.js';
  document.body.appendChild(hierarchy);
})();
