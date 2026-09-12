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
 ['◡','Mucosas orales','Exploración sistemática, mapa anatómico y práctica de descripción.','mucosas'],
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
S.mucosas={k:'GENERAL',t:'MUCOSAS ORALES INTERACTIVAS',i:'Toca una zona, compara con la normalidad y practica una descripción clínica antes de pensar en un diagnóstico.',c:[
 ['Objetivo','Explorar de forma sistemática labios, carrillos, encía, paladar, lengua, piso de boca y orofaringe.'],
 ['Regla didáctica','Describe ubicación, tamaño, color, forma, superficie, bordes, consistencia y síntomas. La app no emite diagnóstico definitivo.']
]};

const ipcState=['0','0','0','0','0','0'];
let ipcSelected=0;
const ihosTeeth=[16,11,26,36,31,46];
const ihosDebris=Object.fromEntries(ihosTeeth.map(t=>[t,0]));
const ihosCalculus=Object.fromEntries(ihosTeeth.map(t=>[t,0]));
let ihosSelected=16;

const mucosaZones={
 labio_superior:{n:'Labio superior',normal:'Rosado, hidratado e íntegro, con contorno regular.',changes:'Fisuras, costras, vesículas, úlceras, edema, resequedad o pigmentación.',explore:'Inspecciona con buena iluminación, eversa el labio y palpa si existe aumento de volumen.'},
 labio_inferior:{n:'Labio inferior',normal:'Rosado, húmedo, blando e íntegro.',changes:'Úlcera traumática, mucocele, queilitis, pigmentación, fisuras o aumento de volumen.',explore:'Eversa el labio y revisa fondo de vestíbulo, frenillo y glándulas menores.'},
 carrillo_derecho:{n:'Carrillo derecho',normal:'Mucosa rosada, húmeda y lisa.',changes:'Línea alba, mordisqueo, úlcera, placa blanca, eritema, masa o cambio salival.',explore:'Separa con espejo, observa de anterior a posterior y palpa si hay una masa.'},
 carrillo_izquierdo:{n:'Carrillo izquierdo',normal:'Mucosa rosada, húmeda y lisa.',changes:'Línea alba, mordisqueo, úlcera, placa blanca, eritema, masa o cambio salival.',explore:'Explora sistemáticamente y compara con el lado contralateral.'},
 encias:{n:'Encía y mucosa alveolar',normal:'Encía firme y de contorno regular; mucosa alveolar móvil y húmeda.',changes:'Eritema, edema, sangrado, recesión, fístula, úlcera o aumento de volumen.',explore:'Inspecciona marginal, papilar y adherida; correlaciona con sondaje cuando corresponda.'},
 paladar_duro:{n:'Paladar duro',normal:'Rosado pálido, firme, queratinizado e íntegro.',changes:'Torus, petequias, placas, úlceras, quemadura, pigmentación o aumento de volumen.',explore:'Usa iluminación directa y palpa cualquier elevación.'},
 paladar_blando:{n:'Paladar blando y úvula',normal:'Rosado, móvil, elevación simétrica y úvula centrada.',changes:'Eritema, petequias, edema, asimetría, exudado, placas o desviación de úvula.',explore:'Pide fonar y observa movilidad, simetría y pilares.'},
 lengua_dorso:{n:'Lengua · dorso',normal:'Rosada, papilada, húmeda y móvil.',changes:'Saburra, depapilación, lengua geográfica/fisurada, placa, úlcera o masa.',explore:'Pide protruir y mover la lengua; revisa toda la superficie.'},
 lengua_lateral:{n:'Lengua · bordes y cara ventral',normal:'Rosada, húmeda, flexible y sin induración.',changes:'Úlcera, placa blanca/roja, masa, induración, pigmentación o lesión vascular.',explore:'Sujeta la punta con gasa, desplaza a ambos lados y palpa bordes laterales.'},
 piso_boca:{n:'Piso de boca',normal:'Rosado, blando, húmedo y sin masas.',changes:'Ránula, aumento de volumen, coloración azulada/rojiza, masa, úlcera o disminución salival.',explore:'Eleva la lengua y realiza palpación bimanual cuando proceda.'},
 orofaringe:{n:'Orofaringe y amígdalas',normal:'Sin inflamación evidente, exudado ni lesiones aparentes.',changes:'Eritema, exudado, hipertrofia, placas, secreción, asimetría o ulceración.',explore:'Inspecciona con buena iluminación y depresor cuando esté indicado.'}
};
let mucosaSelected='labio_superior',mucosaMode='normal',mucosaFinding='Normal',mucosaDraft=[];

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

function addMucosaWidget(){
 const host=document.querySelector('#sheet-content'); if(!host) return;
 const w=document.createElement('div'); w.className='web-clinical-widget mucosa-widget';
 w.innerHTML=`<h4>Práctica interactiva de mucosas</h4><p>Toca una región en el mapa o en la lista.</p>
 <div class="mucosa-layout"><div class="mucosa-map" id="mucosa-map">
 <svg viewBox="0 0 600 430" role="img" aria-label="Mapa esquemático de cavidad oral">
 <ellipse cx="300" cy="215" rx="255" ry="195" fill="#eaa0b2"/><ellipse cx="300" cy="218" rx="215" ry="158" fill="#7f334e"/>
 <ellipse cx="300" cy="125" rx="140" ry="62" fill="#e5a49e"/><ellipse cx="300" cy="285" rx="145" ry="82" fill="#e8848f"/>
 <g fill="#fff">${Array.from({length:8},(_,i)=>`<rect x="${166+i*39}" y="165" width="31" height="40" rx="7"/><rect x="${166+i*39}" y="324" width="31" height="35" rx="7"/>`).join('')}</g>
 <circle id="mucosa-marker" cx="300" cy="58" r="28" fill="#7b4fa7" opacity=".78"/><circle id="mucosa-marker-core" cx="300" cy="58" r="9" fill="#fff"/>
 </svg></div><div><div id="mucosa-zones" class="mucosa-zone-list"></div><div class="mucosa-tabs" id="mucosa-tabs"></div><div class="mucosa-detail" id="mucosa-detail"></div></div></div>
 <div class="mucosa-practice"><strong>Ficha rápida</strong><div id="mucosa-findings" class="mucosa-findings"></div><label>Tamaño aproximado (mm)<input id="mucosa-size" inputmode="decimal" placeholder="ej. 5"></label><label>Observaciones<input id="mucosa-notes" maxlength="120" placeholder="descripción breve"></label><div id="mucosa-writing" class="web-result"></div><button id="mucosa-add" type="button">Agregar al borrador didáctico</button><div id="mucosa-draft"></div></div>`;
 host.appendChild(w);
 const positions={labio_superior:[300,55],labio_inferior:[300,390],carrillo_derecho:[95,220],carrillo_izquierdo:[505,220],encias:[300,185],paladar_duro:[300,120],paladar_blando:[300,180],lengua_dorso:[300,275],lengua_lateral:[410,290],piso_boca:[300,350],orofaringe:[300,205]};
 const draw=()=>{
  const z=w.querySelector('#mucosa-zones');z.innerHTML=Object.entries(mucosaZones).map(([id,o])=>`<button type="button" data-z="${id}" class="${id===mucosaSelected?'active':''}">${o.n}</button>`).join('');
  z.querySelectorAll('button').forEach(b=>b.onclick=()=>{mucosaSelected=b.dataset.z;draw()});
  const [cx,cy]=positions[mucosaSelected];w.querySelector('#mucosa-marker').setAttribute('cx',cx);w.querySelector('#mucosa-marker').setAttribute('cy',cy);w.querySelector('#mucosa-marker-core').setAttribute('cx',cx);w.querySelector('#mucosa-marker-core').setAttribute('cy',cy);
  const tabs=w.querySelector('#mucosa-tabs');tabs.innerHTML=[['normal','Normal'],['changes','Alteraciones'],['explore','Cómo explorar']].map(([k,n])=>`<button data-m="${k}" class="${mucosaMode===k?'active':''}" type="button">${n}</button>`).join('');tabs.querySelectorAll('button').forEach(b=>b.onclick=()=>{mucosaMode=b.dataset.m;draw()});
  const o=mucosaZones[mucosaSelected];w.querySelector('#mucosa-detail').innerHTML=`<strong>${o.n}</strong><p>${o[mucosaMode]}</p>`;
  const f=w.querySelector('#mucosa-findings');f.innerHTML=['Normal','Úlcera','Placa blanca','Placa roja','Eritema','Aumento de volumen','Pigmentación','Vesícula','Fisura/costra','Otro'].map(x=>`<button type="button" data-f="${x}" class="${mucosaFinding===x?'active':''}">${x}</button>`).join('');f.querySelectorAll('button').forEach(b=>b.onclick=()=>{mucosaFinding=b.dataset.f;drawWriting()});
  drawWriting();
 };
 const drawWriting=()=>{
  const o=mucosaZones[mucosaSelected],size=w.querySelector('#mucosa-size').value.trim(),notes=w.querySelector('#mucosa-notes').value.trim();
  const text=mucosaFinding==='Normal'?`${o.n}: ${o.normal}`:`${o.n}: ${mucosaFinding}; tamaño aproximado ${size?size+' mm':'no registrado'}.${notes?' Observaciones: '+notes+'.':''} Registro descriptivo educativo; correlacionar clínicamente antes de establecer un diagnóstico.`;
  w.querySelector('#mucosa-writing').textContent=text;
 };
 w.querySelector('#mucosa-size').addEventListener('input',drawWriting);w.querySelector('#mucosa-notes').addEventListener('input',drawWriting);
 w.querySelector('#mucosa-add').onclick=()=>{mucosaDraft.push(w.querySelector('#mucosa-writing').textContent);w.querySelector('#mucosa-draft').innerHTML='<strong>Borrador didáctico · no se guarda</strong>'+mucosaDraft.map((x,i)=>`<p>${i+1}. ${x}</p>`).join('')};
 w.querySelector('#mucosa-map').onclick=e=>{const r=e.currentTarget.getBoundingClientRect(),x=(e.clientX-r.left)/r.width,y=(e.clientY-r.top)/r.height;const id=y<.18?'labio_superior':y>.84?'labio_inferior':x<.22?'carrillo_derecho':x>.78?'carrillo_izquierdo':y<.38?'paladar_duro':y<.5?'paladar_blando':y<.72?'lengua_dorso':'piso_boca';mucosaSelected=id;draw()};
 draw();
}

const baseRender=renderSheet;
renderSheet=function(k){
 baseRender(k);
 if(k==='ipc') addIpcWidget();
 if(k==='ihos') addIhosWidget();
 if(k==='mucosas') addMucosaWidget();
};
renderSheet('ingreso');
})();
