package edu.ifam.br.ecosemente;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import edu.ifam.br.ecosemente.entity.NomeCientifico;
import edu.ifam.br.ecosemente.entity.Semente;

import edu.ifam.br.ecosemente.repository.NomeCientificoDAO;
import edu.ifam.br.ecosemente.repository.SementeDAO;

public class DetalheSementeActivity extends AppCompatActivity {

    // --- Componentes ---
    private EditText etNomePopular;
    private EditText etTempoMedio;
    private Button btnConfirmar;
    private Button btnLimpar;
    private Button btnDelete; // <-- ADICIONADO DE VOLTA
    private ScrollView svDetalheSemente;
    private ProgressBar pbDetalheSemente;
    private TextView tvNomeCientificoResultado;
    private TextView tvEpocaInicio;
    private TextView tvEpocaFim;
    private RadioGroup rgTipoCultivo;
    private RadioGroup rgTamanhoPorte;
    private TextView tvLatitude, tvLongitude;
    private ImageView ivImagem;

    // --- Componentes (GPS e Câmera) ---
    private Button btnCapturarCoordenadas;
    private Button btnTirarFoto;
    private Button btnGaleria;

    // --- DAOs e Variáveis ---
    private SementeDAO sementeDAO;
    private NomeCientificoDAO nomeCientificoDAO;
    private long id;

    private Long nomeCientificoEncontradoId = null;
    private final Calendar myCalendar = Calendar.getInstance();

    // --- Variáveis de Permissão, GPS e Câmera ---
    private FusedLocationProviderClient fusedLocationClient;
    private ActivityResultLauncher<String[]> locationPermissionLauncher;
    private ActivityResultLauncher<String[]> cameraPermissionLauncher;
    private ActivityResultLauncher<String[]> galleryPermissionLauncher;
    private ActivityResultLauncher<Uri> cameraLauncher;
    private ActivityResultLauncher<String[]> galleryLauncher;

    private double latitude = 0.0;
    private double longitude = 0.0;
    private String caminhoImagem = null;
    private Uri fotoUri = null;

    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_IMAGE_PICK = 2;


    private final String[] locationPermissions = new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
    private final String[] cameraPermissions = new String[]{Manifest.permission.CAMERA};
    private String[] galleryPermissions;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhe_semente);

        // --- Inicialização de DAOs e GPS ---
        sementeDAO = new SementeDAO(this);
        nomeCientificoDAO = new NomeCientificoDAO(this);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        // --- Encontrar Views (Corrigido) ---
        etNomePopular = findViewById(R.id.etCadastroNomePopular);
        etTempoMedio = findViewById(R.id.etCadastroTempoMedioColheita);
        btnConfirmar = findViewById(R.id.btnCadastroConfirmar);
        btnLimpar = findViewById(R.id.btnCadastroLimpar);
        btnDelete = findViewById(R.id.btnDelete); // <-- ADICIONADO DE VOLTA
        svDetalheSemente = findViewById(R.id.svCadastroSemente);
        pbDetalheSemente = findViewById(R.id.pbCadastroSemente);
        tvNomeCientificoResultado = findViewById(R.id.tvNomeCientificoResultado);
        tvEpocaInicio = findViewById(R.id.tvDetalheSementeEpocaInicio);
        tvEpocaFim = findViewById(R.id.tvDetalheSementeEpocaFim);
        rgTipoCultivo = findViewById(R.id.rgCadastroTipoCultivo);
        rgTamanhoPorte = findViewById(R.id.rgCadastroTamanhoPorte);
        tvLatitude = findViewById(R.id.tvDetalheSementeLatitude);
        tvLongitude = findViewById(R.id.tvDetalheSementeLongitude);
        ivImagem = findViewById(R.id.ivDetalheSementeImagem);
        btnCapturarCoordenadas = findViewById(R.id.btnCapturarCoordenadas);
        btnTirarFoto = findViewById(R.id.btnTirarFoto);
        btnGaleria = findViewById(R.id.btnGaleria);

        svDetalheSemente.setVisibility(View.INVISIBLE);
        pbDetalheSemente.setVisibility(View.INVISIBLE);
        btnDelete.setVisibility(View.INVISIBLE); // <-- ADICIONADO DE VOLTA (Esconde por padrão)

        // --- Configurar Permissões e Lançadores ---
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            galleryPermissions = new String[]{Manifest.permission.READ_MEDIA_IMAGES};
        } else {
            galleryPermissions = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE};
        }

        registrarLaunchersPermissao();
        registrarLaunchersAtividade();

        // --- Configurar Listeners ---
        setupDatePickerListeners();
        btnCapturarCoordenadas.setOnClickListener(v -> onCapturarCoordenadasClick());
        btnTirarFoto.setOnClickListener(v -> onTirarFotoClick());
        btnGaleria.setOnClickListener(v -> onGaleriaClick());
        btnLimpar.setOnClickListener(v -> btnClearSementeOnClick(v));

        configurarBuscaNomeCientifico();

        Intent intent = getIntent();
        if(intent.hasExtra("id")){
            // MODO EDIÇÃO
            id = intent.getLongExtra("id",0);
            pbDetalheSemente.setVisibility(View.VISIBLE);

            btnDelete.setVisibility(View.VISIBLE); // <-- ADICIONADO DE VOLTA (Mostra)
            btnConfirmar.setOnClickListener(v -> updateSemente(id));
            btnDelete.setOnClickListener(v -> deleteSemente(id)); // <-- ADICIONADO DE VOLTA

            btnLimpar.setVisibility(View.GONE);

            if (savedInstanceState == null) {
                // Precisamos abrir os DAOs aqui, já que onResume pode
                // não ter rodado ainda ou não vai carregar o getSemente
                sementeDAO.open();
                nomeCientificoDAO.open();
                getSemente(id); // Carrega os dados do DB
                // Não feche os DAOs, onPause() cuidará disso.
            }
        } else {
            // MODO CADASTRO
            svDetalheSemente.setVisibility(View.VISIBLE);
            btnConfirmar.setOnClickListener(v -> saveSemente());
            btnDelete.setVisibility(View.GONE); // <-- ADICIONADO DE VOLTA (Garante que está escondido)
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        sementeDAO.open();
        nomeCientificoDAO.open();
    }

    @Override
    protected void onPause() {
        super.onPause();
        sementeDAO.close();
        nomeCientificoDAO.close();
    }

    // --- LÓGICA DE PERMISSÕES E LANÇADORES ---
    // (Esta parte está correta e permanece igual)
    private void registrarLaunchersPermissao() {
        locationPermissionLauncher = registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(), result -> {
            if (Boolean.TRUE.equals(result.get(Manifest.permission.ACCESS_FINE_LOCATION))) {
                buscarUltimaLocalizacao();
            } else {
                Toast.makeText(this, "Permissão de GPS negada.", Toast.LENGTH_SHORT).show();
            }
        });
        cameraPermissionLauncher = registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(), result -> {
            if (Boolean.TRUE.equals(result.get(Manifest.permission.CAMERA))) {
                abrirCamera();
            } else {
                Toast.makeText(this, "Permissão de Câmera negada.", Toast.LENGTH_SHORT).show();
            }
        });
        galleryPermissionLauncher = registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(), result -> {
            boolean granted = false;
            for (String perm : galleryPermissions) {
                if (Boolean.TRUE.equals(result.get(perm))) {
                    granted = true;
                    break;
                }
            }
            if (granted) {
                abrirGaleria();
            } else {
                Toast.makeText(this, "Permissão de Galeria negada.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void registrarLaunchersAtividade() {
        cameraLauncher = registerForActivityResult(new ActivityResultContracts.TakePicture(), result -> {
            if (result && fotoUri != null) { // Check fotoUri is not null
                Log.d("DetalheSemente", "Camera Result: Success. URI: " + fotoUri);
                // Directly use the URI the camera was told to write to
                this.caminhoImagem = fotoUri.toString(); // Update the member variable FIRST
                ivImagem.setImageURI(null); // Clear previous image first (helps with redraw)
                ivImagem.setImageURI(fotoUri); // Set the new image
                Log.d("DetalheSemente", "Camera: caminhoImagem updated to: " + this.caminhoImagem);
            } else {
                Log.e("DetalheSemente", "Camera Result: Failed or fotoUri is null.");
            }
        });

        galleryLauncher = registerForActivityResult(new ActivityResultContracts.OpenDocument(), uri -> {
            if (uri != null) {
                Log.d("DetalheSemente", "Gallery Result: URI: " + uri);
                try {
                    // Make sure we only ask for READ permission
                    final int takeFlags = Intent.FLAG_GRANT_READ_URI_PERMISSION;
                    // Try taking persistable permission
                    getContentResolver().takePersistableUriPermission(uri, takeFlags);
                    Log.d("DetalheSemente", "Persistable permission requested for: " + uri);

                    // Update the member variable FIRST
                    this.caminhoImagem = uri.toString();
                    ivImagem.setImageURI(null); // Clear previous image
                    ivImagem.setImageURI(uri); // Set the new image
                    Log.d("DetalheSemente", "Gallery: caminhoImagem updated to: " + this.caminhoImagem);

                } catch (SecurityException e) {
                    Log.e("DetalheSemente", "SecurityException taking persistable permission for URI: " + uri, e);
                    // If persistable fails, try to load it directly anyway (might work temporarily)
                    try {
                        this.caminhoImagem = uri.toString();
                        ivImagem.setImageURI(null);
                        ivImagem.setImageURI(uri);
                        Log.w("DetalheSemente", "Loaded gallery image without guaranteed persistent permission for: " + this.caminhoImagem);
                    } catch (Exception innerE) {
                        Log.e("DetalheSemente", "Failed even loading gallery image directly for URI: " + uri, innerE);
                        Toast.makeText(this, "Não foi possível carregar a imagem selecionada.", Toast.LENGTH_SHORT).show();
                        this.caminhoImagem = null; // Clear if loading failed
                        ivImagem.setImageResource(android.R.drawable.ic_menu_gallery); // Reset to default
                    }
                } catch (Exception e) {
                    Log.e("DetalheSemente", "Error processing gallery URI: " + uri, e);
                    Toast.makeText(this, "Erro ao processar a imagem.", Toast.LENGTH_SHORT).show();
                    this.caminhoImagem = null; // Clear on general error
                    ivImagem.setImageResource(android.R.drawable.ic_menu_gallery); // Reset to default
                }
            } else {
                Log.e("DetalheSemente", "Gallery Result: URI is null.");
            }
        });
    }

    // --- LÓGICA DE CLIQUE (GPS/CÂMERA/GALERIA) ---
    // (Esta parte está correta e permanece igual)
    private void onCapturarCoordenadasClick() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            buscarUltimaLocalizacao();
        } else {
            locationPermissionLauncher.launch(locationPermissions);
        }
    }
    private void onTirarFotoClick() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            abrirCamera();
        } else {
            cameraPermissionLauncher.launch(cameraPermissions);
        }
    }
    private void onGaleriaClick() {
        boolean granted = false;
        for (String perm : galleryPermissions) {
            if (ContextCompat.checkSelfPermission(this, perm) == PackageManager.PERMISSION_GRANTED) {
                granted = true;
                break;
            }
        }
        if (granted) {
            abrirGaleria();
        } else {
            galleryPermissionLauncher.launch(galleryPermissions);
        }
    }

    // --- LÓGICA DE EXECUÇÃO (GPS/CÂMERA/GALERIA) ---
    // (Esta parte está correta e permanece igual)
    @SuppressLint("MissingPermission")
    private void buscarUltimaLocalizacao() {
        pbDetalheSemente.setVisibility(View.VISIBLE);
        fusedLocationClient.getLastLocation().addOnSuccessListener(this, location -> {
            if (location != null) {
                latitude = location.getLatitude();
                longitude = location.getLongitude();
                tvLatitude.setText(String.format(Locale.US, "Latitude: %.6f", latitude));
                tvLongitude.setText(String.format(Locale.US, "Longitude: %.6f", longitude));
                pbDetalheSemente.setVisibility(View.GONE);
            } else {
                pbDetalheSemente.setVisibility(View.GONE);
                Toast.makeText(this, "Não foi possível obter a localização. Tente novamente.", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void abrirCamera() {
        File arquivoFoto = null;
        try {
            arquivoFoto = criarArquivoFoto();
        } catch (IOException ex) {
            Toast.makeText(this, "Erro ao criar arquivo de foto", Toast.LENGTH_SHORT).show();
            return;
        }
        if (arquivoFoto != null) {
            fotoUri = FileProvider.getUriForFile(this,
                    "edu.ifam.br.ecosemente.provider",
                    arquivoFoto);
            cameraLauncher.launch(fotoUri);
        }
    }
    private File criarArquivoFoto() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName, ".jpg", storageDir);
        return image;
    }
    private void abrirGaleria() {
        // MUDANÇA: "image/*" deve estar dentro de um array
        galleryLauncher.launch(new String[]{"image/*"});
    }

    // --- LÓGICA DO DATEPICKER ---
    // (Esta parte está correta e permanece igual)
    private void setupDatePickerListeners() {
        tvEpocaInicio.setOnClickListener(v -> showDatePickerDialog(tvEpocaInicio));
        tvEpocaFim.setOnClickListener(v -> showDatePickerDialog(tvEpocaFim));
    }
    private void showDatePickerDialog(final TextView textView) {
        DatePickerDialog.OnDateSetListener dateSetListener = (view, year, month, dayOfMonth) -> {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, month);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel(textView);
        };
        new DatePickerDialog(DetalheSementeActivity.this, dateSetListener,
                myCalendar.get(Calendar.YEAR),
                myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)).show();
    }
    private void updateLabel(TextView textView) {
        String myFormat = "dd/MM/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, new Locale("pt", "BR"));
        textView.setText(sdf.format(myCalendar.getTime()));
    }

    private void configurarBuscaNomeCientifico() {
        etNomePopular.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }

            @Override
            public void afterTextChanged(Editable s) {
                // Evita crash se o DAO não estiver aberto (ex: durante a criação)
                if (nomeCientificoDAO == null) return;

                String nomePopularDigitado = s.toString().trim();
                if (nomePopularDigitado.isEmpty()) {
                    tvNomeCientificoResultado.setText("Não informado");
                    nomeCientificoEncontradoId = null;
                    return;
                }

                // Busca no banco
                NomeCientifico nc = nomeCientificoDAO.buscarPorNomePopular(nomePopularDigitado);
                //System.out.println("Resultado do nome cientifico: " + nc.getNome());

                if (nc != null) {
                    // Encontrou!
                    System.out.println("Resultado do nome cientifico: " + nc.getNome());
                    tvNomeCientificoResultado.setText(nc.getNome());
                    nomeCientificoEncontradoId = nc.getId();
                } else {
                    // Não encontrou
                    System.out.println("Resultado do nome cientifico: NULL para a busca por '" + nomePopularDigitado + "'");
                    tvNomeCientificoResultado.setText("Nome científico não encontrado");
                    nomeCientificoEncontradoId = null;
                }
            }
        });
    }


    // (Este método está correto, pois já removemos os campos)
    private Semente getSementeFromEditText(){
        Semente semente = new Semente();

        semente.setNome(etNomePopular.getText().toString());

        try {
            semente.setTempoMedioColheita(Integer.parseInt(etTempoMedio.getText().toString()));
        } catch (NumberFormatException e) {
            semente.setTempoMedioColheita(0); // Default or handle error
        }

        semente.setNomeCientificoId(this.nomeCientificoEncontradoId);

        semente.setEpocaInicio(tvEpocaInicio.getText().toString());
        semente.setEpocaFim(tvEpocaFim.getText().toString());

        int selectedTipoId = rgTipoCultivo.getCheckedRadioButtonId();
        if (selectedTipoId == R.id.rbCadastroNatural) {
            semente.setTipoCultivo("Natural");
        } else if (selectedTipoId == R.id.rbCadastroPlantada) {
            semente.setTipoCultivo("Plantada");
        } else {
            semente.setTipoCultivo("");
        }

        int selectedTamanhoId = rgTamanhoPorte.getCheckedRadioButtonId();
        if (selectedTamanhoId == R.id.rbCadastroTamanhoPequeno) {
            semente.setTamanhoPorte("Pequeno");
        } else if (selectedTamanhoId == R.id.rbCadastroTamanhoMedio) {
            semente.setTamanhoPorte("Médio");
        } else if (selectedTamanhoId == R.id.rbCadastroTamanhoGrande) {
            semente.setTamanhoPorte("Grande");
        } else {
            semente.setTamanhoPorte("");
        }

        semente.setLatitude(this.latitude);
        semente.setLongitude(this.longitude);

        // --- VERIFICATION ---
        // Log the value just before assigning it to the Semente object
        Log.d("DetalheSemente", "getSementeFromEditText: Assigning caminhoImagem = " + this.caminhoImagem);
        semente.setCaminhoImagem(this.caminhoImagem);
        // --- END VERIFICATION ---

        return semente;
    }

    // (Este método está correto, pois já removemos os campos)
    private void setSementeOnEditText(Semente semente) {
        if (semente == null) return;

        etNomePopular.setText(semente.getNome());
        etTempoMedio.setText(String.valueOf(semente.getTempoMedioColheita()));

        // Se a semente tem um ID científico, buscamos no banco para exibir
        if (semente.getNomeCientificoId() != null) {
            NomeCientifico nc = nomeCientificoDAO.buscarPorId(semente.getNomeCientificoId());
            if (nc != null) {
                tvNomeCientificoResultado.setText(nc.getNome());
                this.nomeCientificoEncontradoId = nc.getId();
            } else {
                // ID existe na semente, mas não no banco (ex: foi deletado)
                tvNomeCientificoResultado.setText("Não encontrado (ID: " + semente.getNomeCientificoId() + ")");
                this.nomeCientificoEncontradoId = null; // Zera para não salvar lixo
            }
        } else {
            // Semente não tem nome científico associado
            tvNomeCientificoResultado.setText("Não informado");
            this.nomeCientificoEncontradoId = null;
        }

        tvEpocaInicio.setText(semente.getEpocaInicio());
        tvEpocaFim.setText(semente.getEpocaFim());

        // --- Lógica CORRETA para marcar RadioButton Tipo Cultivo ---
        if ("Natural".equals(semente.getTipoCultivo())) {
            rgTipoCultivo.check(R.id.rbCadastroNatural);
        } else if ("Plantada".equals(semente.getTipoCultivo())) {
            rgTipoCultivo.check(R.id.rbCadastroPlantada);
        } else {
            rgTipoCultivo.clearCheck(); // Desmarca todos
        }
        // --- FIM DA LÓGICA CORRETA ---

        // --- Lógica CORRETA para marcar RadioButton Tamanho Porte ---
        if ("Pequeno".equals(semente.getTamanhoPorte())) {
            rgTamanhoPorte.check(R.id.rbCadastroTamanhoPequeno);
        } else if ("Médio".equals(semente.getTamanhoPorte())) {
            rgTamanhoPorte.check(R.id.rbCadastroTamanhoMedio);
        } else if ("Grande".equals(semente.getTamanhoPorte())) {
            rgTamanhoPorte.check(R.id.rbCadastroTamanhoGrande);
        } else {
            rgTamanhoPorte.clearCheck(); // Desmarca todos
        }
        // --- FIM DA LÓGICA CORRETA ---


        this.latitude = semente.getLatitude();
        this.longitude = semente.getLongitude();
        this.caminhoImagem = semente.getCaminhoImagem();
        System.out.println("Caminho da foto no setSementeOnEditText: " + semente.getCaminhoImagem());

        tvLatitude.setText(String.format(Locale.US, "Latitude: %.6f", this.latitude));
        tvLongitude.setText(String.format(Locale.US, "Longitude: %.6f", this.longitude));

        // --- Lógica CORRETA para carregar Imagem com try-catch ---
        try {
            if (this.caminhoImagem != null && !this.caminhoImagem.isEmpty()) {
                Uri imageUri = Uri.parse(this.caminhoImagem);
                ivImagem.setImageURI(imageUri);
            } else {
                ivImagem.setImageResource(android.R.drawable.ic_menu_gallery);
            }
        } catch (SecurityException e) {
            Log.e("DetalheSemente", "Permission error loading image URI: " + this.caminhoImagem, e);
            ivImagem.setImageResource(android.R.drawable.ic_menu_gallery); // Imagem padrão em caso de erro
        } catch (Exception e) {
            Log.e("DetalheSemente", "Error loading image URI: " + this.caminhoImagem, e);
            ivImagem.setImageResource(android.R.drawable.ic_menu_gallery); // Imagem padrão em caso de erro
        }
        // --- FIM DA LÓGICA CORRETA ---
    }

    public void btnClearSementeOnClick(View view){

        tvEpocaInicio.setText("");
        tvEpocaInicio.setHint("Data início");
        tvEpocaFim.setText("");
        tvEpocaFim.setHint("Data fim");
        tvLatitude.setText("Latitude: (Aguardando...)");
        tvLongitude.setText("Longitude: (Aguardando...)");
        ivImagem.setImageResource(android.R.drawable.ic_menu_gallery);
        tvNomeCientificoResultado.setText("Não informado");
        this.nomeCientificoEncontradoId = null;

        rgTipoCultivo.clearCheck();
        rgTamanhoPorte.clearCheck();

        this.latitude = 0.0;
        this.longitude = 0.0;
        this.caminhoImagem = null;
    }

    private void getSemente(Long id){
        Semente semente = sementeDAO.getSemente(id);

        if (semente != null) {
            setSementeOnEditText(semente);
        } else {
            Toast.makeText(this, "Semente não encontrada.", Toast.LENGTH_SHORT).show();
            finish();
        }

        pbDetalheSemente.setVisibility(View.INVISIBLE);
        svDetalheSemente.setVisibility(View.VISIBLE);
    }

    // --- LÓGICA DE VALIDAÇÃO E CRUD ---

    // (Este método está correto, pois já removemos os campos)
    private boolean validarCampos(Semente semente) {
        if (semente.getNome() == null || semente.getNome().trim().isEmpty()) {
            Toast.makeText(this, "Nome popular é obrigatório.", Toast.LENGTH_SHORT).show();
            etNomePopular.requestFocus();
            return false;
        }

        if (semente.getTempoMedioColheita() == null || semente.getTempoMedioColheita() <= 0) {
            Toast.makeText(this, "Tempo médio (maior que 0) é obrigatório.", Toast.LENGTH_SHORT).show();
            etTempoMedio.requestFocus();
            return false;
        }

        if (semente.getEpocaInicio() == null || semente.getEpocaInicio().trim().isEmpty() || semente.getEpocaInicio().equals("Data início")) {
            Toast.makeText(this, "Época de início é obrigatória.", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (semente.getEpocaFim() == null || semente.getEpocaFim().trim().isEmpty() || semente.getEpocaFim().equals("Data fim")) {
            Toast.makeText(this, "Época de fim é obrigatória.", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (semente.getTipoCultivo() == null || semente.getTipoCultivo().trim().isEmpty()) {
            Toast.makeText(this, "Tipo de cultivo (Natural/Plantada) é obrigatório.", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (semente.getTamanhoPorte() == null || semente.getTamanhoPorte().trim().isEmpty()) {
            Toast.makeText(this, "Tamanho do porte é obrigatório.", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    // (Este método está correto, pois já removemos os campos)
    private void saveSemente() {
        Semente semente = getSementeFromEditText();

        if (!validarCampos(semente)) {
            return;
        }

        try {
            sementeDAO.insert(semente);
            Toast.makeText(this, "Semente salva com sucesso!", Toast.LENGTH_SHORT).show();
            finish();
        } catch (Exception e) {
            Toast.makeText(this, "Erro ao salvar semente: " + e.getMessage(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    // (Este método está correto, pois já removemos os campos)
    private void updateSemente(Long id) {
        Semente semente = getSementeFromEditText();

        if (!validarCampos(semente)) {
            return;
        }

        try {
            sementeDAO.update(id, semente);
            Toast.makeText(this, "Semente atualizada com sucesso!", Toast.LENGTH_SHORT).show();
            finish();
        } catch (Exception e) {
            Toast.makeText(this, "Erro ao atualizar semente: " + e.getMessage(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    // --- MÉTODO ADICIONADO DE VOLTA ---
    private void deleteSemente(Long id) {
        try {
            sementeDAO.delete(id);
            Toast.makeText(this, "Semente excluída com sucesso!", Toast.LENGTH_SHORT).show();
            finish();
        } catch (Exception e) {
            Toast.makeText(this, "Erro ao excluir semente: " + e.getMessage(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    // --- Lifecycle State Saving ---

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        // Save the current image URI string
        if (caminhoImagem != null) {
            outState.putString("caminhoImagem", caminhoImagem);
        }
        // Save the camera URI if it exists (needed if camera was opened but not confirmed yet)
        if (fotoUri != null) {
            outState.putParcelable("fotoUri", fotoUri);
        }
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        // Restore the image URI string if it was saved
        if (savedInstanceState.containsKey("caminhoImagem")) {
            caminhoImagem = savedInstanceState.getString("caminhoImagem");
            // Update the ImageView if the restored path is valid
            if (caminhoImagem != null) {
                try {
                    Uri restoredUri = Uri.parse(caminhoImagem);
                    // Check if we still have permission (especially for gallery URIs)
                    // Granting read permission again if needed might be complex here.
                    // Often, just trying to load it is sufficient if persistable permission worked.
                    ivImagem.setImageURI(restoredUri);
                } catch (Exception e) {
                    Log.e("DetalheSemente", "Error restoring image URI: " + caminhoImagem, e);
                    ivImagem.setImageResource(android.R.drawable.ic_menu_gallery); // Fallback
                }
            }
        }
        // Restore the camera URI if it was saved
        if (savedInstanceState.containsKey("fotoUri")) {
            fotoUri = savedInstanceState.getParcelable("fotoUri");
        }
    }
    // --- End Lifecycle State Saving ---



}