// v0.32 — quick signs/symptoms and verified pediatric/local-anesthetic calculators.
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
  i:'Cálculos educativos con dosis pediátricas de referencia y presentaciones verificadas en México. No sustituyen la indicación, prescripción ni ficha técnica.',
  c:[
    ['Medicamentos pediátricos','Selecciona varios analgésicos, AINE, antibióticos, nitroimidazoles o antivirales. La frecuencia se expresa como cada cuántas horas.'],
    ['Anestésicos locales','Lidocaína, mepivacaína, articaína y bupivacaína con límites dentales pediátricos AAPD y volumen real del cartucho.'],
    ['Abrir calculadoras','<a class="clinical-tool-link" href="#calculadoras">Ir a las calculadoras ↓</a>']
  ]
};
if(S.ingreso?.items && !S.ingreso.items.some(x=>x[3]==='sintomas')){
  S.ingreso.items.splice(3,0,['🩹','Signos y síntomas','Registro rápido de dolor, evolución y signos asociados.','sintomas']);
  S.ingreso.items.splice(4,0,['🧮','Calculadoras clínicas','Medicamentos pediátricos y anestésicos locales.','calculadoras']);
}
renderSheet('ingreso');

const pediatricCatalogV32=[
  {
    id:'PARACETAMOL',cat:'Analgésicos',name:'Paracetamol / acetaminofén',
    presentations:[
      {id:'PARA_100_ML',label:'Solución oral / gotas 100 mg/mL',mgml:100},
      {id:'PARA_TAB_500',label:'Tableta 500 mg',mgunit:500}
    ],
    regimens:[{id:'PARA_PAIN',label:'Dolor leve/moderado',basis:'dose',min:10,max:15,hours:[4,6],maxDailyKg:75,maxDaily:4000,note:'AAPD: 10–15 mg/kg por dosis cada 4–6 h; máximo 75 mg/kg/día y 4000 mg/24 h.'}],
    source:'México: solución 100 mg/mL y tableta 500 mg documentadas por Secretaría de Salud CDMX/PLM.'
  },
  {
    id:'IBUPROFEN',cat:'AINE',name:'Ibuprofeno',
    presentations:[
      {id:'IBU_100_5',label:'Suspensión 100 mg/5 mL',mgml:20},
      {id:'IBU_40_ML',label:'Suspensión/gotas 40 mg/mL (200 mg/5 mL)',mgml:40},
      {id:'IBU_TAB_200',label:'Tableta/cápsula 200 mg',mgunit:200},
      {id:'IBU_TAB_400',label:'Tableta/cápsula 400 mg',mgunit:400}
    ],
    regimens:[{id:'IBU_PAIN',label:'Dolor/inflamación',basis:'dose',min:4,max:10,hours:[6,8],maxSingle:600,note:'AAPD: 4–10 mg/kg por dosis cada 6–8 h; máximo 600 mg por toma en menores de 12 años.'}],
    source:'México: 100 mg/5 mL y 40 mg/mL documentados por COFEPRIS/Secretaría de Salud; 200–400 mg sólidos por PLM/documentos públicos.'
  },
  {
    id:'NAPROXEN',cat:'AINE',name:'Naproxeno',
    presentations:[
      {id:'NAP_125_5',label:'Suspensión 125 mg/5 mL',mgml:25},
      {id:'NAP_TAB_250',label:'Tableta 250 mg',mgunit:250},
      {id:'NAP_TAB_500',label:'Tableta 500 mg',mgunit:500}
    ],
    regimens:[{id:'NAP_PAIN',label:'Dolor/inflamación',basis:'dose',min:5,max:7,hours:[8,12],maxDaily:1000,note:'AAPD: 5–7 mg/kg por dosis cada 8–12 h; máximo 1000 mg/día.'}],
    source:'México: suspensión 125 mg/5 mL y tableta 250 mg documentadas por Secretaría de Salud CDMX; 250/500 mg por PLM. Verificar edad autorizada del producto.'
  },
  {
    id:'AMOXICILLIN',cat:'Antibióticos',name:'Amoxicilina',
    presentations:[
      {id:'AMOX_250_5',label:'Suspensión 250 mg/5 mL',mgml:50},
      {id:'AMOX_500_5',label:'Suspensión 500 mg/5 mL',mgml:100},
      {id:'AMOX_CAP_500',label:'Cápsula 500 mg',mgunit:500},
      {id:'AMOX_TAB_875',label:'Tableta 875 mg',mgunit:875}
    ],
    regimens:[
      {id:'AMOX_Q8',label:'Cada 8 h',basis:'day',min:20,max:40,hours:[8],maxSingle:500,note:'AAPD: >3 meses y <40 kg: 20–40 mg/kg/día divididos cada 8 h; máximo 500 mg por dosis.'},
      {id:'AMOX_Q12',label:'Cada 12 h',basis:'day',min:25,max:45,hours:[12],maxSingle:875,note:'AAPD: >3 meses y <40 kg: 25–45 mg/kg/día divididos cada 12 h; máximo 875 mg por dosis.'}
    ],
    source:'México: suspensiones 250 y 500 mg/5 mL documentadas en información para prescribir mexicana.'
  },
  {
    id:'AMOX_CLAV_4_1',cat:'Antibióticos',name:'Amoxicilina + ácido clavulánico · 4:1',
    presentations:[
      {id:'AMC_125_31_5',label:'Suspensión 125/31.25 mg por 5 mL',mgml:25},
      {id:'AMC_250_62_5',label:'Suspensión 250/62.5 mg por 5 mL',mgml:50}
    ],
    regimens:[{id:'AMC_4_1_Q8',label:'Relación 4:1 · cada 8 h',basis:'day',min:20,max:40,hours:[8],maxSingle:500,note:'Ficha mexicana: 20–40 mg/kg/día del componente amoxicilina cada 8 h. AAPD: las relaciones 4:1 se administran 3 veces al día.'}],
    source:'CORRECCIÓN: 125/31.25 y 250/62.5 mg/5 mL son 4:1; no deben calcularse como 7:1 cada 12 h.'
  },
  {
    id:'AMOX_CLAV_7_1',cat:'Antibióticos',name:'Amoxicilina + ácido clavulánico · 7:1',
    presentations:[
      {id:'AMC_200_28_5',label:'Suspensión 200/28.5 mg por 5 mL',mgml:40},
      {id:'AMC_400_57_5',label:'Suspensión 400/57 mg por 5 mL',mgml:80},
      {id:'AMC_TAB_875',label:'Tableta 875/125 mg',mgunit:875}
    ],
    regimens:[{id:'AMC_7_1_Q12',label:'Relación 7:1 · cada 12 h',basis:'day',min:25,max:45,hours:[12],maxSingle:875,maxDaily:1750,note:'AAPD: 25–45 mg/kg/día de amoxicilina cada 12 h; máximo 875 mg por dosis y 1750 mg/día. Las relaciones 7:1 se administran 2 veces al día.'}],
    source:'México: 200/28.5 y 400/57 mg/5 mL documentadas. Se excluye 600/42.9 mg/5 mL porque usa una relación/esquema especial y no es intercambiable.'
  },
  {
    id:'AZITHROMYCIN',cat:'Antibióticos',name:'Azitromicina',
    presentations:[{id:'AZI_200_5',label:'Suspensión 200 mg/5 mL',mgml:40},{id:'AZI_TAB_500',label:'Tableta 500 mg',mgunit:500}],
    regimens:[
      {id:'AZI_DAY1',label:'Día 1',basis:'dose',min:10,max:12,hours:[24],maxSingle:500,note:'AAPD: 10–12 mg/kg una vez el día 1; máximo 500 mg.'},
      {id:'AZI_LATER',label:'Días 2–5',basis:'dose',min:5,max:6,hours:[24],maxSingle:250,note:'AAPD: 5–6 mg/kg cada 24 h; máximo 250 mg por toma.'},
      {id:'AZI_PERIO',label:'Periodontal · casos seleccionados',basis:'dose',min:10,max:12,hours:[24],maxSingle:500,note:'AAPD: 10–12 mg/kg cada 24 h durante 3 días en casos periodontales seleccionados con alergia a penicilina.'}
    ],
    source:'México: suspensión 200 mg/5 mL y tableta 500 mg documentadas por PLM.'
  },
  {
    id:'CEPHALEXIN',cat:'Antibióticos',name:'Cefalexina',
    presentations:[
      {id:'CEPH_125_5',label:'Suspensión 125 mg/5 mL',mgml:25},{id:'CEPH_250_5',label:'Suspensión 250 mg/5 mL',mgml:50},
      {id:'CEPH_CAP_250',label:'Cápsula 250 mg',mgunit:250},{id:'CEPH_CAP_500',label:'Cápsula 500 mg',mgunit:500}
    ],
    regimens:[
      {id:'CEPH_MILD',label:'Leve/moderada',basis:'day',min:25,max:50,hours:[6,8,12],maxDaily:2000,note:'AAPD: 25–50 mg/kg/día divididos cada 6–12 h; máximo 2000 mg/día.'},
      {id:'CEPH_SEVERE',label:'Grave',basis:'day',min:75,max:100,hours:[6,8],maxDaily:4000,note:'AAPD: 75–100 mg/kg/día divididos cada 6–8 h; máximo 4000 mg/día.'}
    ],
    source:'México: suspensiones 125/5 y 250/5 mg/mL y cápsulas 250/500 mg documentadas por PLM.'
  },
  {
    id:'CLARITHROMYCIN',cat:'Antibióticos',name:'Claritromicina',
    presentations:[
      {id:'CLARI_125_5',label:'Suspensión 125 mg/5 mL',mgml:25},{id:'CLARI_250_5',label:'Suspensión 250 mg/5 mL',mgml:50},
      {id:'CLARI_TAB_250',label:'Tableta 250 mg',mgunit:250},{id:'CLARI_TAB_500',label:'Tableta 500 mg',mgunit:500}
    ],
    regimens:[{id:'CLARI_Q12',label:'Cada 12 h',basis:'dose',min:7.5,max:7.5,hours:[12],maxSingle:500,note:'AAPD: 7.5 mg/kg por dosis cada 12 h; máximo 500 mg.'}],
    source:'México: suspensiones 125 y 250 mg/5 mL y tabletas 250/500 mg documentadas por PLM.'
  },
  {
    id:'CLINDAMYCIN',cat:'Antibióticos',name:'Clindamicina',
    presentations:[{id:'CLINDA_75_5',label:'Solución/suspensión 75 mg/5 mL',mgml:15},{id:'CLINDA_CAP_150',label:'Cápsula 150 mg',mgunit:150},{id:'CLINDA_CAP_300',label:'Cápsula 300 mg',mgunit:300}],
    regimens:[{id:'CLINDA_SOFT',label:'Tejidos blandos',basis:'day',min:20,max:30,hours:[8],maxSingle:450,note:'AAPD: 20–30 mg/kg/día divididos cada 8 h; máximo 450 mg por dosis.'}],
    source:'México: DALACIN C 75 mg/5 mL cuenta con información/registro mexicano. Riesgo de colitis por C. difficile; no se recomienda para profilaxis de endocarditis dental.'
  },
  {
    id:'METRONIDAZOLE',cat:'Nitroimidazoles',name:'Metronidazol',
    presentations:[
      {id:'METRO_125_5',label:'Suspensión 125 mg/5 mL',mgml:25},{id:'METRO_250_5',label:'Suspensión 250 mg/5 mL',mgml:50},
      {id:'METRO_TAB_250',label:'Tableta 250 mg',mgunit:250},{id:'METRO_TAB_500',label:'Tableta 500 mg',mgunit:500}
    ],
    regimens:[{id:'METRO_PERIO',label:'Periodontal / gingivitis necrosante · casos seleccionados',basis:'dose',min:10,max:10,hours:[8],maxSingle:250,note:'AAPD: 10 mg/kg por dosis cada 8 h durante 7 días; máximo 250 mg. No ofrece una pauta pediátrica para infección odontógena anaerobia aislada.'}],
    source:'México: suspensiones 125 y 250 mg/5 mL documentadas por PLM.'
  },
  {
    id:'ACYCLOVIR',cat:'Antivirales',name:'Aciclovir',
    presentations:[
      {id:'ACY_200_5',label:'Suspensión 200 mg/5 mL',mgml:40},{id:'ACY_TAB_200',label:'Tableta 200 mg',mgunit:200},{id:'ACY_TAB_400',label:'Tableta 400 mg',mgunit:400},
      {id:'ACY_CREAM_5',label:'Crema 5% · tópica; sin cálculo por kg',topical:true}
    ],
    regimens:[
      {id:'ACY_PRIMARY',label:'Gingivoestomatitis herpética primaria',basis:'dose',min:20,max:20,hours:[6],maxSingle:800,note:'AAPD: 20 mg/kg por dosis 4 veces/día por 5–7 días; si se espacian uniformemente equivale a cada 6 h; máximo 800 mg por toma. Uso no aprobado por FDA para esta indicación.'},
      {id:'ACY_LABIAL',label:'Herpes labial sistémico',basis:'dose',min:20,max:20,hours:[6],maxSingle:400,note:'AAPD: 20 mg/kg por dosis 4 veces/día; máximo 400 mg por toma.'}
    ],
    source:'México: suspensión 200 mg/5 mL, tabletas 200/400 mg y crema 5% documentadas por PLM.'
  },
  {
    id:'VALACYCLOVIR',cat:'Antivirales',name:'Valaciclovir',
    presentations:[{id:'VALA_TAB_500',label:'Comprimido 500 mg',mgunit:500},{id:'VALA_TAB_1000',label:'Comprimido 1000 mg',mgunit:1000}],
    regimens:[{id:'VALA_PRIMARY',label:'Gingivoestomatitis herpética primaria',basis:'dose',min:20,max:20,hours:[12],maxSingle:1000,note:'AAPD: ≥3 meses, 20 mg/kg por dosis cada 12 h por 5–7 días; máximo 1000 mg por toma. Uso no aprobado por FDA para esta indicación.'}],
    source:'México: comprimidos 500 y 1000 mg documentados por PLM; no se añadió suspensión comercial no verificada.'
  }
];

const anestheticsV32=[
  {id:'lidocaine-epi',name:'Lidocaína 2% con epinefrina',pct:2,max:4.4,ratios:[100000,50000],note:'AAPD: máximo dental pediátrico conservador 4.4 mg/kg. En México está documentado el cartucho dental 2% + epinefrina 1:100,000 de 1.8 mL.'},
  {id:'lidocaine-plain',name:'Lidocaína 2% sola',pct:2,max:4.4,ratios:[],note:'AAPD: 4.4 mg/kg como máximo dental pediátrico conservador. Verifica presentación y volumen del producto real.'},
  {id:'mepivacaine-plain',name:'Mepivacaína 3% sin vasoconstrictor',pct:3,max:4.4,ratios:[],note:'AAPD: mepivacaína 3% sola, máximo dental 4.4 mg/kg. La tabla actual no lista mepivacaína 2% con epinefrina; lista 2% + levonordefrina 1:20,000.'},
  {id:'articaine',name:'Articaína 4% con epinefrina',pct:4,max:7,ratios:[100000,200000],minAge:4,note:'AAPD: 7 mg/kg; 1:100,000 o 1:200,000. No recomendada en menores de 4 años.'},
  {id:'bupivacaine',name:'Bupivacaína 0.5% con epinefrina',pct:.5,max:1.3,ratios:[200000],minAge:12,note:'AAPD: máximo dental 1.3 mg/kg; no recomendada en menores de 12 años.'}
];

function num(id){return Number(document.querySelector(id)?.value||0)}
function fixed(n,d=2){return Number.isFinite(n)?n.toFixed(d):'—'}
function rangeText(a,b,d=1){return Math.abs(a-b)<.005?fixed(a,d):`${fixed(a,d)}–${fixed(b,d)}`}
function esc(s){return String(s).replace(/[&<>"']/g,c=>({'&':'&amp;','<':'&lt;','>':'&gt;','"':'&quot;',"'":'&#39;'}[c]))}

const pedSelectionV32=new Set();
const pedStateV32={};

function injectPediatricCalculatorV32(){
  const card=document.querySelector('#calculadoras .calculator-card');
  if(!card)return;
  card.innerHTML=`
    <h3>🧒 Medicamentos pediátricos</h3>
    <p>Escribe el peso una sola vez y selecciona uno o varios medicamentos. Las dosis de referencia se basan en AAPD y las presentaciones mostradas se contrastaron con fuentes mexicanas. La frecuencia se expresa como <strong>cada cuántas horas</strong>.</p>
    <div class="calculator-fields"><label class="full">Peso (kg)<input id="ped-weight-v32" type="number" min="0" step="0.1" inputmode="decimal"></label></div>
    <div class="calc-warning"><strong>Uso educativo:</strong> la calculadora no decide la indicación, duración ni medicamento. La dosis pediátrica no debe exceder el límite adulto aplicable y debe ajustarse a edad, alergias, función renal/hepática, interacciones y ficha técnica.</div>
    <div id="ped-drug-list-v32" class="calculator-fields"></div>
    <div id="ped-results-v32"></div>`;

  const groups=[...new Set(pediatricCatalogV32.map(m=>m.cat))];
  const list=document.querySelector('#ped-drug-list-v32');
  list.innerHTML=groups.map(cat=>{
    const meds=pediatricCatalogV32.filter(m=>m.cat===cat);
    return `<div class="full"><strong>${esc(cat)}</strong>${meds.map(m=>`<label><input type="checkbox" data-ped-drug="${m.id}"> ${esc(m.name)}</label>`).join('')}</div>`;
  }).join('');

  document.querySelector('#ped-weight-v32')?.addEventListener('input',renderPediatricResultsV32);
  list.addEventListener('change',e=>{
    const id=e.target?.dataset?.pedDrug;
    if(!id)return;
    if(e.target.checked)pedSelectionV32.add(id);else pedSelectionV32.delete(id);
    renderPediatricResultsV32();
  });
  document.querySelector('#ped-results-v32')?.addEventListener('change',e=>{
    const id=e.target?.dataset?.med;
    const role=e.target?.dataset?.role;
    if(!id||!role)return;
    pedStateV32[id]=pedStateV32[id]||{};
    pedStateV32[id][role]=Number(e.target.value);
    renderPediatricResultsV32();
  });
  renderPediatricResultsV32();
}

function calcPedV32(weight,reg,hours,pres){
  if(!(weight>0&&hours>0)||pres.topical||!reg.hours.includes(hours))return null;
  const doses=24/hours;
  let lo=reg.basis==='dose'?weight*reg.min:weight*reg.min/doses;
  let hi=reg.basis==='dose'?weight*reg.max:weight*reg.max/doses;
  if(reg.maxDailyKg)hi=Math.min(hi,reg.maxDailyKg*weight/doses);
  if(reg.maxDaily)hi=Math.min(hi,reg.maxDaily/doses);
  if(reg.maxSingle)hi=Math.min(hi,reg.maxSingle);
  if(lo>hi)lo=hi;
  return {lo,hi,dailyLo:lo*doses,dailyHi:hi*doses,mlLo:pres.mgml?lo/pres.mgml:null,mlHi:pres.mgml?hi/pres.mgml:null,uLo:pres.mgunit?lo/pres.mgunit:null,uHi:pres.mgunit?hi/pres.mgunit:null};
}

function renderPediatricResultsV32(){
  const out=document.querySelector('#ped-results-v32');
  if(!out)return;
  const weight=Number(document.querySelector('#ped-weight-v32')?.value||0);
  if(!pedSelectionV32.size){out.innerHTML='<div class="calc-result"><strong>Selecciona uno o varios medicamentos.</strong><span>Se mostrarán presentación, mg/kg, máximo e intervalo en horas.</span></div>';return;}
  out.innerHTML=pediatricCatalogV32.filter(m=>pedSelectionV32.has(m.id)).map(m=>{
    const state=pedStateV32[m.id]||(pedStateV32[m.id]={reg:0,pres:0,hours:m.regimens[0].hours[0]});
    state.reg=Math.min(Number(state.reg)||0,m.regimens.length-1);
    state.pres=Math.min(Number(state.pres)||0,m.presentations.length-1);
    const reg=m.regimens[state.reg],pres=m.presentations[state.pres];
    if(!reg.hours.includes(Number(state.hours)))state.hours=reg.hours[0];
    const hours=Number(state.hours);
    const calc=calcPedV32(weight,reg,hours,pres);
    const basis=reg.basis==='dose'?'mg/kg por dosis':'mg/kg por día';
    const maxes=[reg.maxSingle?`Máx. por toma: ${reg.maxSingle} mg`:'',reg.maxDailyKg?`Máx. por peso: ${reg.maxDailyKg} mg/kg/día`:'',reg.maxDaily?`Máx. absoluto: ${reg.maxDaily} mg/24 h`:''].filter(Boolean).join(' · ');
    const result=pres.topical
      ? '<span>Presentación tópica: no se convierte a mg/kg. Verifica pauta específica por edad e indicación.</span>'
      : !calc
        ? '<span>Introduce un peso válido para calcular.</span>'
        : `<strong>${rangeText(calc.lo,calc.hi)} mg por toma</strong>${calc.mlLo!=null?`<span>${rangeText(calc.mlLo,calc.mlHi,2)} mL cada ${hours} h</span>`:''}${calc.uLo!=null?`<span>Equivalente matemático: ${rangeText(calc.uLo,calc.uHi,2)} unidad(es) cada ${hours} h; no implica que una tableta/cápsula pueda fraccionarse.</span>`:''}<span>Total equivalente: ${rangeText(calc.dailyLo,calc.dailyHi)} mg/24 h</span>`;
    return `<article class="calculator-card" style="margin-top:16px">
      <h3>${esc(m.name)}</h3>
      <label>Presentación<select data-med="${m.id}" data-role="pres">${m.presentations.map((p,i)=>`<option value="${i}" ${i===state.pres?'selected':''}>${esc(p.label)}</option>`).join('')}</select></label>
      ${m.regimens.length>1?`<label>Esquema<select data-med="${m.id}" data-role="reg">${m.regimens.map((r,i)=>`<option value="${i}" ${i===state.reg?'selected':''}>${esc(r.label)}</option>`).join('')}</select></label>`:`<p><strong>${esc(reg.label)}</strong></p>`}
      <p><strong>Dosis de referencia:</strong> ${rangeText(reg.min,reg.max)} ${basis}</p>
      ${maxes?`<p>${esc(maxes)}</p>`:''}
      <label>¿Cada cuántas horas?<select data-med="${m.id}" data-role="hours">${reg.hours.map(h=>`<option value="${h}" ${h===hours?'selected':''}>Cada ${h} h</option>`).join('')}</select></label>
      <div class="calc-warning">${esc(reg.note)}</div>
      <div class="calc-result">${result}</div>
      <div class="calc-warning"><strong>Verificación:</strong> ${esc(m.source)}</div>
      ${(m.cat==='Antibióticos'||m.cat==='Nitroimidazoles')?'<div class="calc-warning">Los antibióticos no se indican por dolor dental aislado ni para procesos virales. Confirma una indicación infecciosa apropiada y aplica uso prudente de antimicrobianos.</div>':''}
    </article>`;
  }).join('');
}

function fillAnestheticOptions(){
  const select=document.querySelector('#la-drug');
  if(!select)return;
  select.innerHTML=anestheticsV32.map((a,i)=>`<option value="${i}">${a.name}</option>`).join('');
  updateRatioOptions();
}
function updateRatioOptions(){
  const index=Number(document.querySelector('#la-drug')?.value||0);
  const spec=anestheticsV32[index];
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
  const spec=anestheticsV32[index];
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
  const ageWarning=spec.minAge&&age>0&&age<spec.minAge?`<span><strong>⚠ Alerta de edad:</strong> esta referencia no recomienda ${esc(spec.name)} por debajo de ${spec.minAge} años.</span>`:'';
  out.innerHTML=`<strong>${fixed(mgcart,1)} mg de anestésico por cartucho</strong><span>Máximo dental por peso (${spec.max} mg/kg): ${fixed(maxmg,1)} mg</span><span>Máximo teórico por anestésico: ${fixed(carts)} cartuchos</span><span>Cartuchos enteros sin rebasar ese máximo por peso: ${whole}</span>${epi?`<span>Epinefrina por cartucho: ${fixed(epi,1)} µg</span>`:''}${ageWarning}`;
}

function relabelLegacyPediatricCard(){
  injectPediatricCalculatorV32();
  const heading=document.querySelector('#calculadoras .section-heading p:last-child');
  if(heading)heading.textContent='Los cálculos se realizan localmente. Dosis de referencia: AAPD; presentaciones: fuentes mexicanas (COFEPRIS/Secretaría de Salud CDMX/PLM). No sustituyen prescripción, ficha técnica ni juicio clínico.';
}

const quickForm=document.querySelector('#quick-symptom-form');
quickForm?.addEventListener('input',()=>{
  const value=id=>document.querySelector(id)?.value?.trim()||'___';
  const summary=document.querySelector('#quick-symptom-summary');
  if(summary)summary.textContent=`Localización: ${value('#qs-location')}. Inicio: ${value('#qs-onset')}. Evolución: ${value('#qs-evolution')}. Intensidad: ${value('#qs-intensity')}/10. Carácter: ${value('#qs-character')}. Asociados/notas: ${value('#qs-notes')}.`;
});

document.querySelector('#la-drug')?.addEventListener('change',updateRatioOptions);
['#la-weight','#la-age','#la-cartridge','#la-ratio'].forEach(sel=>{
  document.querySelector(sel)?.addEventListener('input',calculateAnestheticWeb);
  document.querySelector(sel)?.addEventListener('change',calculateAnestheticWeb);
});

relabelLegacyPediatricCard();
fillAnestheticOptions();
calculateAnestheticWeb();
