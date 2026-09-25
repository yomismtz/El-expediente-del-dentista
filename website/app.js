const S={
 ingreso:{k:'HOJA 2',t:'NOTA DE INGRESO',i:'Toca cada apartado para abrir su explicación o el módulo interactivo relacionado.',items:[
  ['●','Identificación','Cómo se llena la identificación y cómo redactar los datos generales.','identificacion'],
  ['✚','ASA','Clasificación sistémica y antecedentes que apoyan la categoría.','historia'],
  ['◉','Medicamentos','Medicamentos actuales, vía, esquema referido e implicaciones odontológicas.','historia'],
  ['◌','ATM y músculos','Apertura, dolor, ruidos, trayectoria y exploración muscular.','atm'],
  ['◇','Oclusión','Angle, plano terminal, canina, overjet, overbite y mordidas.','oclusion'],
  ['◡','Mucosas','Exploración de tejidos blandos y descripción de hallazgos.','mucosas'],
  ['+','CPOD / ceod','Experiencia de caries por dentición.','cpod'],
  ['⌇','Diagnóstico periodontal','Integra índices, sondaje y hallazgos periodontales.','periodonto'],
  ['⚡','Pulpar / periapical','Diagnósticos separados, pruebas que los sustentan y redacción.','pulpar'],
  ['⌒','Prótesis','Edentulismo, Kennedy y necesidades protésicas.','protesis']
 ]},
 identificacion:{k:'GENERAL',t:'IDENTIFICACIÓN',i:'Qué registrar y cómo redactarlo de forma clara.',c:[['Datos generales','Edad, sexo registrado, ocupación y demás rubros que solicita el formato físico.'],['Motivo de consulta','Usa una frase breve y fiel al motivo referido.'],['Padecimiento actual','Inicio, evolución, síntomas, desencadenantes y factores que modifican el cuadro.']]},
 historia:{k:'GENERAL',t:'ANAMNESIS / ASA',i:'Los antecedentes positivos requieren ampliación.',c:[['Antecedente positivo','Inicio/estado actual, tratamiento, medicamentos y complicaciones relevantes.'],['ASA','Clasificación educativa integrada con el resto de la valoración.'],['Medicamentos','Nombre genérico, dosis o esquema referido, vía y motivo de uso; confirmar con la fuente clínica correspondiente.']]},
 mucosas:{k:'GENERAL',t:'MUCOSAS',i:'Explora por zonas y describe normalidad o cambios observados.',c:[['Secuencia','Labios → carrillos → encía → lengua → piso de boca → paladar → orofaringe.'],['Redacción','Localización, color, superficie, consistencia, tamaño y síntomas cuando exista lesión.']]},
 oclusion:{k:'GENERAL',t:'OCLUSIÓN',i:'Relaciona dentición y hallazgos oclusales.',c:[['Permanente','Angle, relación canina, líneas medias, overjet, overbite y alteraciones.'],['Temporal','Plano terminal y relación canina cuando corresponda.']]},
 vitales:{k:'GENERAL',t:'SIGNOS VITALES',i:'Registra y compara de forma educativa con el grupo de edad correspondiente.',c:[['Incluye','FR, FC, TA, temperatura, peso, talla, IMC y glucosa capilar cuando proceda.'],['Técnica','La app explica cómo obtener cada medición antes de interpretarla.']]},
 auxiliares:{k:'GENERAL',t:'ANÁLISIS Y AUXILIARES',i:'Ayuda a comprender por qué se pide cada estudio y cómo interpretar resultados educativos.',c:[['Laboratorio','Biometría hemática, química sanguínea de 18 elementos, perfil tiroideo, coagulación, histopatología, microbiología y CAMBRA.'],['Imagenología','Periapical, bitewing, oclusal, panorámica, cefalométrica, CBCT y resonancia magnética, con lectura sistemática.'],['Ortodoncia','Steiner simplificado, Bolton, Moyers, Tanaka–Johnston, Powell, Nolla y análisis fotográfico.'],['Flujo salival / sialometría','Flujo estimulado y no estimulado, métodos de obtención, tira de papel y criterios educativos de interpretación.'],['Calculadoras','IMC, anestésicos, conversión de dosis, medicamentos pediátricos y orientación sobre fluoruros y clorhexidina.']]},
 odontograma:{k:'ANÁLISIS',t:'ODONTOGRAMA POR CUADRANTES',i:'Los dientes se agrupan en cuatro cuadrantes para reducir desplazamientos.',c:[['Interacción','Selecciona un diente y marca una o varias superficies.'],['Leyenda','Caries, restauración, sellador, ausencia, corona, endodoncia, fractura y extracción indicada.'],['Módulo completo','<a class="inline-module-link" href="#odontograma">Abrir odontograma interactivo por cuadrantes ↓</a>']]},
 icdas:{k:'ANÁLISIS',t:'ICDAS',i:'Registro por superficie con códigos 0–6.',c:[['Varias caras','Un diente puede tener códigos distintos por superficie.'],['Resumen','El código general del diente utiliza el de mayor relevancia entre sus caras.']]},
 oleary:{k:'ANÁLISIS',t:'O’LEARY',i:'Marcado de placa por cuatro superficies.',c:[['Caras','Vestibular, lingual/palatina, mesial y distal.'],['Resultado','Superficies con placa / superficies evaluables × 100.']]},
 ipc:{k:'ANÁLISIS',t:'IPC',i:'El IPC se registra por sextantes, no como una tira de dientes.',c:[['Unidad','Seis sextantes.'],['Registro','Conserva el hallazgo de mayor código dentro de cada sextante.']]},
 ihos:{k:'ANÁLISIS',t:'IHOS',i:'Índice con dientes y superficies específicas.',c:[['Detritos','Código 0–3.'],['Cálculo','Código 0–3 y promedio correspondiente.']]},
 periodonto:{k:'ANÁLISIS',t:'PERIODONTO',i:'Sondaje y hallazgos por diente.',c:[['Sitios','Seis sitios por diente.'],['Hallazgos','Sangrado, placa, supuración, recesión, movilidad y furcación.']]},
 pulpar:{k:'ANÁLISIS',t:'DIAGNÓSTICO PULPAR',i:'Checklist de síntomas y pruebas para orientación educativa.',c:[['Posibles diagnósticos','Pulpa normal, pulpitis reversible, irreversible sintomática/asintomática, necrosis, terapia iniciada y previamente tratado.'],['Redacción','Órgano dentario + diagnóstico pulpar, sustentado por síntomas y pruebas.']]},
 periapical:{k:'ANÁLISIS',t:'DIAGNÓSTICO PERIAPICAL',i:'Se integra por separado del diagnóstico pulpar.',c:[['Posibles diagnósticos','Tejidos apicales normales, periodontitis apical sintomática/asintomática, absceso apical agudo/crónico y osteítis condensante.'],['Redacción','Órgano dentario + diagnóstico periapical + hallazgos que lo apoyan.']]},
 endo:{k:'TRATAMIENTO',t:'FICHA ENDODÓNTICA',i:'Integra pulpar, periapical, longitud de trabajo y secuencia educativa.',c:[['Diagnóstico','Pulpar y periapical en la misma ficha, manteniéndolos separados.'],['Procedimiento','Acceso, conductometría, preparación, irrigación, obturación y restauración según el caso y protocolo docente.']]},
 protesis:{k:'TRATAMIENTO',t:'PRÓTESIS',i:'Arcadas y clasificación de Kennedy como apoyo de aprendizaje.',c:[['Arcadas','Marca dientes presentes y ausentes en maxilar y mandíbula.'],['Kennedy','La app orienta la clase y recuerda aplicar las reglas de Applegate.']]},
 cirugia:{k:'TRATAMIENTO',t:'CIRUGÍA',i:'Explica qué documentar antes, durante y después del procedimiento.',c:[['Antes','Antecedentes, estudios indicados, diagnóstico y sitio.'],['Después','Procedimiento, hemostasia, indicaciones, signos de alarma y seguimiento.']]},
 evolucion:{k:'TRATAMIENTO',t:'NOTAS DE EVOLUCIÓN',i:'Ejemplos genéricos de redacción por procedimiento.',c:[['Estructura','Fecha, procedimiento, hallazgos, materiales/medidas relevantes, indicaciones y seguimiento.'],['Biblioteca','Ingreso, alta, baja, restauraciones, endodoncia, pulpotomía, pulpectomía, extracción, cirugía, profilaxis y otros procedimientos de pregrado.']]}
};

const rightPaper=document.querySelector('#right-paper');
let turnBusy=false;
function renderSheet(k){
 const d=S[k]||S.ingreso;
 document.querySelector('#sheet-kicker').textContent=d.k;
 document.querySelector('#sheet-title').textContent=d.t;
 document.querySelector('#sheet-intro').textContent=d.i;
 const container=document.querySelector('#sheet-content');
 container.className=d.items?'intake-grid':'form-lines';
 if(d.items){
  container.innerHTML=d.items.map(x=>`<button class="intake-item" type="button" data-sheet="${x[3]}"><span class="icon">${x[0]}</span><span><strong>${x[1]}</strong><small>${x[2]}</small></span><span class="arrow">›</span></button>`).join('');
  container.querySelectorAll('[data-sheet]').forEach(b=>b.addEventListener('click',()=>turnTo(b.dataset.sheet)));
 }else{
  container.innerHTML=d.c.map(x=>`<div class="info-block"><strong>${x[0]}</strong><p>${x[1]}</p></div>`).join('');
 }
 document.querySelectorAll('.side-tabs button').forEach(b=>b.classList.toggle('active',b.dataset.sheet===k));
}
function turnTo(k){
 if(turnBusy)return;
 turnBusy=true;
 rightPaper.classList.remove('flip-in');
 rightPaper.classList.add('flip-out');
 setTimeout(()=>{
  renderSheet(k);
  rightPaper.classList.remove('flip-out');
  void rightPaper.offsetWidth;
  rightPaper.classList.add('flip-in');
  setTimeout(()=>{rightPaper.classList.remove('flip-in');turnBusy=false},360);
 },270);
}
document.querySelectorAll('.side-tabs button').forEach(b=>b.addEventListener('click',()=>turnTo(b.dataset.sheet)));
renderSheet('ingreso');

document.querySelectorAll('#open-folder,#open-folder-2').forEach(b=>b?.addEventListener('click',()=>document.querySelector('#expediente')?.scrollIntoView({behavior:'smooth'})));
document.querySelector('#back-to-cover')?.addEventListener('click',()=>document.querySelector('#inicio')?.scrollIntoView({behavior:'smooth'}));

const permanent=[
 ['Q1 · superior derecho',[18,17,16,15,14,13,12,11]],
 ['Q2 · superior izquierdo',[21,22,23,24,25,26,27,28]],
 ['Q4 · inferior derecho',[48,47,46,45,44,43,42,41]],
 ['Q3 · inferior izquierdo',[31,32,33,34,35,36,37,38]]
];
const primary=[
 ['Q5 · superior derecho',[55,54,53,52,51]],
 ['Q6 · superior izquierdo',[61,62,63,64,65]],
 ['Q8 · inferior derecho',[85,84,83,82,81]],
 ['Q7 · inferior izquierdo',[71,72,73,74,75]]
];
let dentition='permanent',selectedTooth=16;
const findings={};
function drawQuadrants(){
 const qs=dentition==='permanent'?permanent:primary;
 const wrap=document.querySelector('#quadrants');
 if(!qs.flatMap(q=>q[1]).includes(selectedTooth)) selectedTooth=qs[0][1][2]||qs[0][1][0];
 wrap.innerHTML=qs.map(q=>`<article class="quadrant"><h4>${q[0]}</h4><div class="teeth ${dentition==='primary'?'primary':''}">${q[1].map(t=>`<button class="tooth-btn ${t===selectedTooth?'active':''}" data-tooth="${t}" type="button"><span class="tooth-shape">🦷</span><b>${t}</b></button>`).join('')}</div></article>`).join('');
 wrap.querySelectorAll('[data-tooth]').forEach(b=>b.addEventListener('click',()=>{selectedTooth=Number(b.dataset.tooth);drawQuadrants();updateSelected()}));
 updateSelected();
}
function updateSelected(){
 document.querySelector('#selected-tooth').textContent=`OD ${selectedTooth}`;
 document.querySelectorAll('.finding-buttons button').forEach(b=>b.classList.toggle('active',findings[selectedTooth]?.has(b.dataset.finding)));
}
document.querySelectorAll('[data-dentition]').forEach(b=>b.addEventListener('click',()=>{
 dentition=b.dataset.dentition;
 document.querySelectorAll('[data-dentition]').forEach(x=>x.classList.toggle('active',x===b));
 drawQuadrants();
}));
document.querySelectorAll('.finding-buttons button').forEach(b=>b.addEventListener('click',()=>{
 findings[selectedTooth]??=new Set();
 const set=findings[selectedTooth];
 set.has(b.dataset.finding)?set.delete(b.dataset.finding):set.add(b.dataset.finding);
 updateSelected();
}));
drawQuadrants();

fetch('build-info.json',{cache:'no-store'}).then(r=>r.ok?r.json():Promise.reject()).then(i=>{
 const s=(i.commit||'').slice(0,7),d=i.builtAt?new Date(i.builtAt).toLocaleString('es-MX',{dateStyle:'medium',timeStyle:'short'}):'fecha no disponible';
 const x=`Última compilación: ${d}${s?` · ${s}`:''}`;
 document.querySelector('#build-label').textContent=x;
 document.querySelector('#build-details').textContent=`Compilada automáticamente desde GitHub · ${d}${s?` · commit ${s}`:''}`;
}).catch(()=>{
 document.querySelector('#build-label').textContent='Compilación disponible al publicar el sitio';
 document.querySelector('#build-details').textContent='La información aparecerá al desplegar GitHub Pages.';
});
