// v0.26 — quick signs/symptoms and educational clinical calculators.
// All values stay in this browser page; nothing here submits clinical data.

S.sintomas={
  k:'ACCESO RÁPIDO',
  t:'SIGNOS Y SÍNTOMAS',
  i:'Registro breve del cuadro actual sin tener que recorrer toda la historia clínica.',
  c:[
    ['Qué registrar','Dolor, localización, inicio, evolución, intensidad, carácter, desencadenantes/alivio y signos asociados.'],
    ['Signos asociados','Aumento de volumen, sangrado, fiebre, limitación de apertura, alteración sensitiva y disfagia.'],
    ['Registro interactivo','<a class="clinical-tool-link" href="#signos-sintomas">Abrir registro rápido ↓</a>']
  ]
};
S.calculadoras={
  k:'HERRAMIENTAS',
  t:'CALCULADORAS CLÍNICAS',
  i:'Cálculos educativos de dosis pediátrica y anestésicos locales. No sustituyen la indicación, prescripción ni la ficha técnica.',
  c:[
    ['Dosis pediátrica','Peso + dosis indicada en mg/kg + frecuencia + concentración. La app no elige el medicamento ni la dosis.'],
    ['Anestésicos locales','Lidocaína ± epinefrina, mepivacaína ± epinefrina y articaína, usando límites pediátricos conservadores y el volumen real del cartucho.'],
    ['Abrir calculadoras','<a class="clinical-tool-link" href="#calculadoras">Ir a las calculadoras ↓</a>']
  ]
};
if(S.ingreso?.items && !S.ingreso.items.some(x=>x[3]==='sintomas')){
  S.ingreso.items.splice(3,0,['🩹','Signos y síntomas','Registro rápido de dolor, evolución y signos asociados.','sintomas']);
  S.ingreso.items.splice(4,0,['🧮','Calculadoras clínicas','Dosis pediátrica y anestésicos locales.','calculadoras']);
}
renderSheet('ingreso');

const anestheticsV26=[
  {id:'lidocaine-epi',name:'Lidocaína 2% con epinefrina',pct:2,max:4.4,ratios:[100000,50000],note:'AAPD usa 4.4 mg/kg como máximo dental pediátrico conservador.'},
  {id:'lidocaine-plain',name:'Lidocaína 2% sola',pct:2,max:4.4,ratios:[],note:'Sin vasoconstrictor. Verifica la ficha técnica de la presentación disponible.'},
  {id:'mepivacaine-plain',name:'Mepivacaína 3%',pct:3,max:4.4,ratios:[],note:'AAPD usa 4.4 mg/kg como máximo dental pediátrico conservador.'},
  {id:'mepivacaine-epi',name:'Mepivacaína 2% con epinefrina',pct:2,max:4.4,ratios:[100000],note:'Existen presentaciones internacionales 2% + epinefrina 1:100,000; en otros mercados la mepivacaína 2% puede usar levonordefrina. Verifica el cartucho real.'},
  {id:'articaine',name:'Articaína 4% con epinefrina',pct:4,max:7,ratios:[100000,200000],minAge:4,note:'No recomendada en menores de 4 años; verifica la ficha técnica local.'}
];

function num(id){return Number(document.querySelector(id)?.value||0)}
function fixed(n,d=2){return Number.isFinite(n)?n.toFixed(d):'—'}

function calculatePediatricWeb(){
  const weight=num('#ped-weight'),mgkg=num('#ped-mgkg'),conc=num('#ped-concentration');
  const freq=Math.trunc(num('#ped-frequency'));
  const basis=document.querySelector('#ped-basis')?.value||'dose';
  const out=document.querySelector('#ped-result');
  if(!out)return;
  if(!(weight>0&&mgkg>0&&conc>0&&freq>0)){
    out.innerHTML='<strong>Completa peso, dosis indicada, frecuencia y concentración con valores mayores que cero.</strong>';
    return;
  }
  const daily=basis==='day'?weight*mgkg:weight*mgkg*freq;
  const dose=basis==='day'?daily/freq:weight*mgkg;
  const ml=dose/conc;
  out.innerHTML=`<strong>Por dosis: ${fixed(dose)} mg</strong><span>Volumen por dosis: ${fixed(ml)} mL</span><span>Total diario matemático: ${fixed(daily)} mg/día</span>`;
}

function fillAnestheticOptions(){
  const select=document.querySelector('#la-drug');
  if(!select)return;
  select.innerHTML=anestheticsV26.map((a,i)=>`<option value="${i}">${a.name}</option>`).join('');
  updateRatioOptions();
}
function updateRatioOptions(){
  const index=Number(document.querySelector('#la-drug')?.value||0);
  const spec=anestheticsV26[index];
  const wrap=document.querySelector('#la-ratio-wrap');
  const select=document.querySelector('#la-ratio');
  if(!wrap||!select||!spec)return;
  if(spec.ratios.length===0){wrap.hidden=true;select.innerHTML='';}
  else{
    wrap.hidden=false;
    select.innerHTML=spec.ratios.map(r=>`<option value="${r}">1:${r.toLocaleString('en-US')}</option>`).join('');
  }
  const note=document.querySelector('#la-note');
  if(note)note.textContent=spec.note;
  calculateAnestheticWeb();
}
function calculateAnestheticWeb(){
  const index=Number(document.querySelector('#la-drug')?.value||0);
  const spec=anestheticsV26[index];
  const weight=num('#la-weight'),cart=num('#la-cartridge'),age=num('#la-age');
  const ratio=Number(document.querySelector('#la-ratio')?.value||0);
  const out=document.querySelector('#la-result');
  if(!out||!spec)return;
  if(!(weight>0&&cart>0)){
    out.innerHTML='<strong>Introduce un peso y el volumen real del cartucho.</strong>';
    return;
  }
  const mgml=spec.pct*10;
  const mgcart=mgml*cart;
  const maxmg=weight*spec.max;
  const carts=maxmg/mgcart;
  const whole=Math.max(0,Math.floor(carts));
  const epi=ratio>0?(1000000/ratio)*cart:null;
  const ageWarning=spec.minAge&&age>0&&age<spec.minAge?`<span><strong>⚠ Alerta de edad:</strong> esta referencia no recomienda articaína por debajo de ${spec.minAge} años.</span>`:'';
  out.innerHTML=`<strong>${fixed(mgcart,1)} mg de anestésico por cartucho</strong><span>Máximo conservador por peso (${spec.max} mg/kg): ${fixed(maxmg,1)} mg</span><span>Máximo teórico por anestésico: ${fixed(carts)} cartuchos</span><span>Cartuchos enteros sin rebasar ese máximo por peso: ${whole}</span>${epi?`<span>Epinefrina por cartucho: ${fixed(epi,1)} µg</span>`:''}${ageWarning}`;
}

['#ped-weight','#ped-mgkg','#ped-concentration','#ped-frequency','#ped-basis'].forEach(sel=>{
  document.querySelector(sel)?.addEventListener('input',calculatePediatricWeb);
  document.querySelector(sel)?.addEventListener('change',calculatePediatricWeb);
});
document.querySelector('#la-drug')?.addEventListener('change',updateRatioOptions);
['#la-weight','#la-age','#la-cartridge','#la-ratio'].forEach(sel=>{
  document.querySelector(sel)?.addEventListener('input',calculateAnestheticWeb);
  document.querySelector(sel)?.addEventListener('change',calculateAnestheticWeb);
});
fillAnestheticOptions();
calculatePediatricWeb();
calculateAnestheticWeb();

const quickForm=document.querySelector('#quick-symptom-form');
quickForm?.addEventListener('input',()=>{
  const value=id=>document.querySelector(id)?.value?.trim()||'___';
  const summary=document.querySelector('#quick-symptom-summary');
  if(summary)summary.textContent=`Localización: ${value('#qs-location')}. Inicio: ${value('#qs-onset')}. Evolución: ${value('#qs-evolution')}. Intensidad: ${value('#qs-intensity')}/10. Carácter: ${value('#qs-character')}. Asociados/notas: ${value('#qs-notes')}.`;
});
