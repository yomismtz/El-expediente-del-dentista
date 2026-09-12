(()=>{
if(typeof S==='undefined'||typeof renderSheet==='undefined') return;

S.ingreso={k:'HOJA 2',t:'NOTA DE INGRESO',i:'Toca cada apartado para saber qué va ahí, cómo se obtiene el dato y cómo puede redactarse en el expediente físico.',items:[
 ['▤','Identificación del paciente','Datos generales, motivo de consulta y padecimiento actual.','identificacion'],
 ['♥','Signos vitales','TA, FC, FR, temperatura, peso, talla, IMC y glucosa capilar cuando corresponda.','vitales'],
 ['✚','Diagnóstico sistémico + ASA','Antecedentes relevantes y clasificación sustentada por la valoración.','historia'],
 ['◉','Medicamentos','Nombre genérico, vía y esquema referido; se enseña a documentar, no a prescribir.','medicamentos'],
 ['◌','ATM y músculos','Dolor, ruidos, apertura, trayectoria y palpación muscular.','atm'],
 ['🦷','Caries y anomalías','Resumen apoyado por odontograma e ICDAS por superficies.','odontograma'],
 ['◇','Oclusión','Angle, canina, plano terminal, overjet, overbite, líneas medias y mordidas.','oclusion'],
 ['◡','Mucosas orales','Exploración sistemática y descripción de hallazgos.','mucosas'],
 ['+','CPOD / ceod','Resultado del índice según la dentición.','cpod'],
 ['⌇','Diagnóstico periodontal','Integra IPC, IHOS, periodontograma y hallazgos.','periodonto'],
 ['⚡','Pulpar / periapical','Diagnósticos separados y pruebas que los sustentan.','pulpar'],
 ['⌒','Prótesis / Kennedy','Edentulismo parcial y clasificación orientativa por arcada.','protesis'],
 ['⚗','Auxiliares diagnósticos','Imagenología, laboratorio, histopatología y otros estudios disponibles.','auxiliares'],
 ['▤','Pronóstico y plan inicial','Problemas prioritarios, estudios pendientes, tratamiento inicial y seguimiento.','pronostico']
]};

S.medicamentos={k:'GENERAL',t:'MEDICAMENTOS QUE ESTÁ TOMANDO',i:'Aprende a documentar exactamente lo que el paciente refiere; esta sección no prescribe tratamientos.',c:[
 ['Qué registrar','Nombre genérico, presentación o dosis si la conoce, vía, frecuencia/esquema referido y motivo de uso.'],
 ['Ejemplos frecuentes','Metformina, insulina, losartán, enalapril, AAS, clopidogrel, anticoagulantes, salbutamol o levotiroxina pueden aparecer en la anamnesis.'],
 ['Ejemplo de redacción','“Refiere metformina por vía oral; presentación y frecuencia según receta/envase referido”. Si no se conoce un dato, no se inventa.']
]};
S.pronostico={k:'GENERAL',t:'PRONÓSTICO Y PLAN INICIAL',i:'Integra lo prioritario al final de la nota, sin convertir la app en una orden clínica.',c:[
 ['Pronóstico','Expresa el pronóstico sólo con la información disponible; si faltan pruebas, indícalo.'],
 ['Plan inicial','Problemas prioritarios, estudios o interconsultas pendientes, medidas iniciales y seguimiento bajo supervisión docente.'],
 ['Redacción educativa','“Pronóstico pendiente de completar ____. Plan inicial: completar diagnóstico y revisar alternativas con el docente”.']
]};

S.ipc={k:'ANÁLISIS',t:'IPC POR SEXTANTES',i:'El IPC se registra por seis sextantes y conserva el peor hallazgo de cada uno.',c:[
 ['Guía visual','<img class="clinical-visual" src="assets/clinical/ipc_visual_guide.webp" alt="Guía visual de IPC por sextantes, códigos y sonda OMS">'],
 ['Interacción','Selecciona un sextante y después su código 0, 1, 2, 3, 4 o X. El resumen se construye automáticamente.']
]};
S.ihos={k:'ANÁLISIS',t:'IHOS INTERACTIVO',i:'Evalúa los seis dientes índice, asigna detritos y cálculo de 0 a 3 y obtén ID-S, IC-S e IHOS.',c:[
 ['Guía visual','<img class="clinical-visual" src="assets/clinical/ihos_visual_guide.webp" alt="Guía visual de IHOS con dientes índice, detritos, cálculo y fórmula">'],
 ['Dientes índice','16V · 11V · 26V · 36L · 31V · 46L.']
]};

const ipcState=['0','0','0','0','0','0'];
let ipcSelected=0;
const ihosTeeth=[16,11,26,36,31,46];
const ihosDebris=Object.fromEntries(ihosTeeth.map(t=>[t,0]));
const ihosCalculus=Object.fromEntries(ihosTeeth.map(t=>[t,0]));
let ihosSelected=16;

function addIpcWidget(){
 const host=document.querySelector('#sheet-content'); if(!host) return;
 const w=document.createElement('div'); w.className='web-clinical-widget';
 w.innerHTML='<h4>Práctica interactiva IPC</h4><p>Toca un sextante y registra el peor hallazgo observado.</p><div class="web-sextants" id="web-ipc-sextants"></div><div class="web-code-row" id="web-ipc-codes"></div><div class="web-summary" id="web-ipc-summary"></div>';
 host.appendChild(w);
 const draw=()=>{
  const s=w.querySelector('#web-ipc-sextants');
  const labels=['S1 · 17–14','S2 · 13–23','S3 · 24–27','S4 · 47–44','S5 · 43–33','S6 · 34–37'];
  s.innerHTML=labels.map((x,i)=>`<button type="button" class="web-sextant ${i===ipcSelected?'active':''}" data-i="${i}">${x}<br><strong>${ipcState[i]}</strong></button>`).join('');
  s.querySelectorAll('button').forEach(b=>b.onclick=()=>{ipcSelected=Number(b.dataset.i);draw()});
  const c=w.querySelector('#web-ipc-codes');
  c.innerHTML=['0','1','2','3','4','X'].map(x=>`<button type="button" class="${ipcState[ipcSelected]===x?'active':''}" data-c="${x}">${x}</button>`).join('');
  c.querySelectorAll('button').forEach(b=>b.onclick=()=>{ipcState[ipcSelected]=b.dataset.c;draw()});
  w.querySelector('#web-ipc-summary').textContent=ipcState.map((x,i)=>`S${i+1}=${x}`).join(' · ');
 };
 draw();
}

function addIhosWidget(){
 const host=document.querySelector('#sheet-content'); if(!host) return;
 const w=document.createElement('div'); w.className='web-clinical-widget';
 w.innerHTML='<h4>Práctica interactiva IHOS</h4><p>Selecciona el diente índice y asigna un grado de detritos y de cálculo.</p><div class="web-ihos-teeth" id="web-ihos-teeth"></div><strong id="web-ihos-label"></strong><p>Detritos</p><div class="web-score" id="web-debris"></div><p>Cálculo</p><div class="web-score" id="web-calculus"></div><div class="web-result" id="web-ihos-result"></div>';
 host.appendChild(w);
 const draw=()=>{
  const t=w.querySelector('#web-ihos-teeth');
  t.innerHTML=ihosTeeth.map(x=>`<button type="button" class="${x===ihosSelected?'active':''}" data-t="${x}">🦷 ${x}${[36,46].includes(x)?'L':'V'}</button>`).join('');
  t.querySelectorAll('button').forEach(b=>b.onclick=()=>{ihosSelected=Number(b.dataset.t);draw()});
  w.querySelector('#web-ihos-label').textContent=`OD ${ihosSelected} · ${[36,46].includes(ihosSelected)?'lingual':'vestibular'}`;
  const score=(id,obj)=>{const e=w.querySelector(id);e.innerHTML=[0,1,2,3].map(x=>`<button type="button" class="${obj[ihosSelected]===x?'active':''}" data-v="${x}">${x}</button>`).join('');e.querySelectorAll('button').forEach(b=>b.onclick=()=>{obj[ihosSelected]=Number(b.dataset.v);draw()})};
  score('#web-debris',ihosDebris); score('#web-calculus',ihosCalculus);
  const avg=o=>ihosTeeth.reduce((a,t)=>a+o[t],0)/ihosTeeth.length;
  const ids=avg(ihosDebris),ics=avg(ihosCalculus),total=ids+ics;
  const state=total<=1.2?'Bueno':total<=3?'Regular':'Malo';
  w.querySelector('#web-ihos-result').textContent=`ID-S ${ids.toFixed(2)} · IC-S ${ics.toFixed(2)} · IHOS ${total.toFixed(2)} · ${state}`;
 };
 draw();
}

const baseRender=renderSheet;
renderSheet=function(k){
 baseRender(k);
 if(k==='ipc') addIpcWidget();
 if(k==='ihos') addIhosWidget();
};
renderSheet('ingreso');
})();
