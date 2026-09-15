// v0.33 — folder hierarchy aligned with the Android app and the clinical-record mockups.
(() => {
  const lime='#8cff28', aqua='#62c3bf';
  const historyColors=['#f36f2e','#ed941f','#e2b719','#c7d20d','#86b91a','#5aa718','#2e8b26','#247f25','#1d7229'];

  const style=document.createElement('style');
  style.textContent=`
    .folder-v33{display:grid;grid-template-columns:1fr 1fr;gap:22px;padding:22px;background:#fff;border:1px solid #d8d8d8}
    .folder-v33 h3{margin:0 0 10px;font-size:1rem}.folder-v33-col{display:grid;grid-template-columns:repeat(3,minmax(0,1fr));gap:12px;align-content:start}
    .folder-v33-card{min-height:92px;border:2px solid #17333a;border-radius:2px;padding:10px;font:700 .88rem/1.25 inherit;cursor:pointer;color:#000;display:flex;align-items:center;justify-content:center;text-align:center}
    .folder-v33-card.lime{background:${lime}}.folder-v33-card.aqua{background:${aqua}}
    .folder-v33-submenu{padding:22px;background:#fff;border:1px solid #d8d8d8}.folder-v33-subgrid{display:grid;grid-template-columns:repeat(4,minmax(0,1fr));gap:16px}
    .folder-v33-history-card{min-height:128px;border:1px solid #4a4a4a;color:#fff;padding:12px;text-align:left;cursor:pointer;font-weight:700}
    .folder-v33-history-card small{display:block;margin-top:7px;font-weight:500;line-height:1.35}.folder-v33-back{margin:0 0 14px;padding:9px 14px;border:1px solid #333;background:#fff;cursor:pointer;font-weight:700}
    .folder-v33-odonto{grid-template-columns:repeat(3,minmax(0,1fr))}.folder-v33-note{grid-column:1/-1;margin:8px 0 0;color:#4b5563;font-size:.92rem}
    @media(max-width:900px){.folder-v33{grid-template-columns:1fr}.folder-v33-col{grid-template-columns:repeat(2,minmax(0,1fr))}.folder-v33-subgrid{grid-template-columns:repeat(2,minmax(0,1fr))}}
    @media(max-width:520px){.folder-v33-col,.folder-v33-subgrid,.folder-v33-odonto{grid-template-columns:1fr}.folder-v33-card{min-height:70px}}
  `;
  document.head.appendChild(style);

  const left=[
    ['Autorización de actividades','actividades'],['Diagnóstico y Tratamiento','tratamiento'],['Tratamiento por Sesiones','sesiones'],
    ['Ficha endodóntica','endo'],['Ficha protésica','protesis'],['Ficha Periodontal (Periodontograma)','periodonto'],
    ['Ficha quirúrgica','cirugia'],['Ficha Trastornos Temporomandibulares','atm-v33'],['Signos y Síntomas','#signos-sintomas'],
    ['Calculadora','#calculadoras'],['O’Leary','oleary'],['CAMBRA','cambra-v33']
  ];
  const right=[
    ['Ficha de identificación','identificacion'],['Historia Clínica','history-v33'],['Examen de mucosas','mucosas'],
    ['Auxiliares de diagnóstico','auxiliares'],['Odontograma (Exámenes de diagnóstico)','odonto-v33'],['Presupuesto','presupuesto'],
    ['Consentimiento informado','consentimiento'],['Solicitud de Tratamiento','solicitud'],['Notas de Evolución','evolucion'],
    ['Exploración Cabeza y Cuello','cabeza-cuello-v33'],['Exploración Articulación temporomandibular','atm-v33'],['Exploración / Examen de Oclusión','oclusion'],
    ['Exploración de Anomalías Dentales','anomalias-v33'],['Exploración anomalías dentales de erupción','erupcion-v33'],['Hábitos y Parafunciones','habitos-v33']
  ];

  S['cambra-v33']={k:'RIESGO DE CARIES',t:'CAMBRA',i:'Caries Management by Risk Assessment: evaluación del riesgo para orientar prevención y manejo.',c:[
    ['Indicadores de enfermedad','Lesiones o cavitación reciente, restauraciones recientes y otros datos de actividad de caries.'],
    ['Factores de riesgo','Exposición frecuente a carbohidratos fermentables, flujo salival reducido y otros factores biológicos/contextuales.'],
    ['Factores protectores','Fluoruro, higiene, control dietético y otras medidas preventivas individualizadas.'],
    ['Edad','Usa la herramienta correspondiente a 0–6 años o >6 años; los factores no se ponderan igual en todas las edades.']
  ]};
  S['atm-v33']={k:'EXPLORACIÓN',t:'ARTICULACIÓN TEMPOROMANDIBULAR',i:'Registro de movimientos, dolor y ruidos articulares.',c:[
    ['Movimientos','Apertura/cierre, lateralidad derecha/izquierda, protrusión y retrusión.'],['Mediciones','Apertura máxima, DVO y DVR cuando el formato lo solicite.'],
    ['Hallazgos','Dolor, chasquido, crepitación, desviación, limitación o bloqueo.']
  ]};
  S['cabeza-cuello-v33']={k:'EXPLORACIÓN',t:'CABEZA Y CUELLO',i:'Exploración organizada por regiones.',c:[
    ['Cráneo','Forma, simetría, palpación y observaciones objetivas.'],['Cara','Perfil, simetría, piel y expresión.'],
    ['Músculos','Músculos faciales y de la masticación.'],['Cuello y ganglios','Inspección, movilidad, palpación y cadenas ganglionares.']
  ]};
  S['anomalias-v33']={k:'HISTORIA / EXAMEN',t:'ANOMALÍAS DENTALES',i:'Registrar presencia, órgano dentario y descripción.',c:[
    ['Número','Aumento o disminución.'],['Tamaño','Macrodoncia / microdoncia.'],['Forma','Localizada o generalizada.'],
    ['Estructura','Esmalte, dentina o cemento.'],['Color','Intrínseco o extrínseco.']
  ]};
  S['erupcion-v33']={k:'HISTORIA / EXAMEN',t:'ALTERACIONES DE ERUPCIÓN Y POSICIÓN',i:'Registrar el diente y la localización.',c:[
    ['Erupción ectópica / transposición','Describir diente y posición.'],['Erupción tardía','Retención, impactación o inclusión.'],['Erupción precoz','Diente natal o neonatal cuando corresponda.']
  ]};
  S['habitos-v33']={k:'HISTORIA',t:'HÁBITOS Y PARAFUNCIONES',i:'Registrar presencia, duración/frecuencia y manifestaciones clínicas.',c:[
    ['Hábitos','Succión digital, interposición lingual, succión labial, respiración bucal, onicofagia y chupón.'],['Parafunción','Bruxismo / rechinamiento.'],['Contexto','Desde cuándo, frecuencia, desencadenantes y manifestaciones faciales u odontológicas.']
  ]};

  const shell=document.querySelector('.folder-shell');
  const stage=shell?.querySelector('.folder-stage');
  const toolbar=shell?.querySelector('.folder-toolbar');
  if(!shell||!stage||!toolbar)return;

  const menu=document.createElement('div');
  menu.className='folder-v33';
  menu.innerHTML=`<section><h3>Hoja izquierda · tratamiento y herramientas</h3><div class="folder-v33-col" data-col="left"></div></section><section><h3>Hoja derecha · historia y exploración</h3><div class="folder-v33-col" data-col="right"></div></section>`;
  shell.insertBefore(menu,stage);
  stage.hidden=true;

  const indexButton=document.createElement('button');
  indexButton.type='button'; indexButton.className='toolbar-button'; indexButton.textContent='‹ Índice del expediente'; indexButton.hidden=true;
  toolbar.prepend(indexButton);

  function showIndex(){menu.hidden=false;stage.hidden=true;indexButton.hidden=true;}
  function showStage(id){menu.hidden=true;stage.hidden=false;indexButton.hidden=false;if(S[id])renderSheet(id);}
  indexButton.addEventListener('click',showIndex);

  function historyMenu(){
    const items=[
      ['I. IDENTIFICACIÓN DEL PACIENTE','Nombre · edad · sexo/género · religión · ocupación.','identificacion'],
      ['II. MOTIVO DE CONSULTA Y PADECIMIENTO ACTUAL','Motivo, inicio, evolución y características.','historia'],
      ['IV. ANTECEDENTES HEREDO-FAMILIARES','Antecedentes relevantes en familiares.','historia'],
      ['V. ANTECEDENTES PERSONALES NO PATOLÓGICOS','Hogar · hábitos · higiene bucal/general · alcohol · tabaco · tatuajes.','historia'],
      ['VII. ANTECEDENTES PERSONALES PATOLÓGICOS','Enfermedades, alergias, medicamentos, hospitalizaciones y ASA.','historia'],
      ['VIII. ANTECEDENTES QUIRÚRGICOS Y TRAUMÁTICOS','Cirugías, traumatismos y eventos relevantes.','historia'],
      ['IX. EXPLORACIÓN FÍSICA','Signos vitales · signos/síntomas · exploración general · marcha · actitud.','vitales'],
      ['ANTECEDENTES DE TRATAMIENTOS ORTODÓNTICOS','Tipo de aparato · tiempo de uso · motivo de indicación.','historia']
    ];
    menu.className='folder-v33-submenu';
    menu.innerHTML=`<button class="folder-v33-back" type="button">‹ Regresar al expediente</button><h2>Historia Clínica</h2><div class="folder-v33-subgrid">${items.map((x,i)=>`<button class="folder-v33-history-card" style="background:${historyColors[i]}" data-target="${x[2]}">${x[0]}<small>${x[1]}</small></button>`).join('')}</div>`;
    menu.querySelector('.folder-v33-back').onclick=buildIndex;
    menu.querySelectorAll('[data-target]').forEach(b=>b.onclick=()=>showStage(b.dataset.target));
  }

  function odontogramMenu(){
    const items=[['Odontograma clínico','#odontograma'],['CPOD · dentición permanente','cpod'],['ceod · dentición temporal','cpod'],['ICDAS','icdas'],['IHOS','ihos'],['IPC','ipc']];
    menu.className='folder-v33-submenu';
    menu.innerHTML=`<button class="folder-v33-back" type="button">‹ Regresar al expediente</button><h2>Odontograma · Exámenes de diagnóstico</h2><div class="folder-v33-subgrid folder-v33-odonto">${items.map(x=>`<button class="folder-v33-card aqua" data-target="${x[1]}">${x[0]}</button>`).join('')}<p class="folder-v33-note">CPOD/ceod resumen experiencia de caries por dentición; ICDAS registra severidad por superficie. IHOS e IPC se conservan como índices independientes.</p></div>`;
    menu.querySelector('.folder-v33-back').onclick=buildIndex;
    menu.querySelectorAll('[data-target]').forEach(b=>b.onclick=()=>openTarget(b.dataset.target));
  }

  function openTarget(target){
    if(target==='history-v33'){historyMenu();return;}
    if(target==='odonto-v33'){odontogramMenu();return;}
    if(target.startsWith('#')){document.querySelector(target)?.scrollIntoView({behavior:'smooth'});return;}
    showStage(target);
  }

  function fillColumn(selector,items,klass){
    const col=menu.querySelector(selector); if(!col)return;
    col.innerHTML=items.map(x=>`<button type="button" class="folder-v33-card ${klass}" data-target="${x[1]}">${x[0]}</button>`).join('');
    col.querySelectorAll('[data-target]').forEach(b=>b.onclick=()=>openTarget(b.dataset.target));
  }

  function buildIndex(){
    menu.className='folder-v33';
    menu.innerHTML=`<section><h3>Hoja izquierda · tratamiento y herramientas</h3><div class="folder-v33-col" data-col="left"></div></section><section><h3>Hoja derecha · historia y exploración</h3><div class="folder-v33-col" data-col="right"></div></section>`;
    fillColumn('[data-col="left"]',left,'lime'); fillColumn('[data-col="right"]',right,'aqua'); showIndex();
  }

  buildIndex();
})();
