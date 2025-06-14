//package com.example.springbootconcepts.controllers;
//
//import com.allstate.bpmn.framework.runtime.activity.model.TransientData;
//import com.allstate.bpmn.framework.runtime.activity.start.BPMNProcessStarterRegistry;
//import com.allstate.bpmn.framework.runtime.activity.start.model.ProcessResult;
//import com.example.springbootconcepts.domains.Address;
//import com.example.springbootconcepts.domains.Employee;
//import com.example.springbootconcepts.dto.EmployeeDto;
//import com.example.springbootconcepts.mappers.EmployeeMapper;
//import com.example.springbootconcepts.repos.EmployeeRepository;
//import com.example.springbootconcepts.services.employeeServices.EmployeeService;
//import com.fasterxml.jackson.core.type.TypeReference;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.google.gson.Gson;
//import com.google.gson.GsonBuilder;
//import com.google.gson.stream.JsonReader;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.ArgumentCaptor;
//import org.mockito.Captor;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.MediaType;
//import org.springframework.http.ResponseEntity;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.setup.MockMvcBuilders;
//import org.springframework.util.ResourceUtils;
//
//import java.io.File;
//import java.io.FileNotFoundException;
//import java.io.FileReader;
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.stream.Collectors;
//
//import static org.hamcrest.MatcherAssert.assertThat;
//import static org.hamcrest.Matchers.hasKey;
//import static org.hamcrest.Matchers.is;
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertNotNull;
//import static org.mockito.Mockito.*;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
//
//@ExtendWith(MockitoExtension.class)
//class EmployeeControllerTest {
//
//    @InjectMocks
//    private EmployeeController subject;
//
//    @Mock
//    private EmployeeService employeeService;
//    @Mock
//    private EmployeeMapper employeeMapper;
//    @Mock
//    private BPMNProcessStarterRegistry mockStarterRegistry;
//    @Mock
//    private ProcessResult mockProcessResult;
//    @Mock
//    private TransientData mockTransientData;
//    @Mock
//    private EmployeeRepository employeeRepository;
//
//    Gson gson;
//
//    private EmployeeDto postRequest;
//
//
//    private EmployeeDto postResponse;
//
//    HashMap<String,Object> mockResponse;
//
//    HashMap<String,Object> variables;
//
//    private Employee sampleRequest;
//
//    private ResponseEntity<Object> sampleResponse;
//
//    private EmployeeDto samRequest;
//    private List<EmployeeDto> employeeDtoList;
//
//    private ResponseEntity<Employee> samResponse;
//
//    private Employee testRequest;
//
//    MockMvc mockMvc;
//
//    @Autowired
//    private ObjectMapper mapper;
//
//
//    @Captor
//    ArgumentCaptor<EmployeeDto> stringArgumentCaptor;
//
//
//
//
//    @BeforeEach
//    void setUp() throws IOException {
//        variables = setDefaultHashMapVariables();
//        mockProcessResult = ProcessResult.of(variables, mockTransientData);
//        mockResponse = setDefaultHashMapForResponse();
//        testRequest = toGenerateTestRequest();
//        postResponse = toGenerateResponse();
//        sampleRequest = toGenerateExceptionObjects();
//        samResponse = toGenerateSampleRequest();
//        employeeDtoList = toGenerateListOfEmployees();
//        this.mockMvc = MockMvcBuilders.standaloneSetup(new EmployeeController(mockStarterRegistry,employeeService)).build();
//    }
//
//    @Test
//    void testGetAllEmployees(){
//    //    when(employeeService.getAllEmployees()).thenReturn(employeeDtoList);
//        try {
//            mockMvc.perform(get("/api/employee"))
//                    .andExpectAll(
//                            status().isOk(),
//                            content().contentType("text/html"),
//                            forwardedUrl("/WEB-INF/layouts/main.jsp")
//                    );
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//    }
//    @Test
//    public void controllerTest(){
//        System.out.println("test started");
//    }
//    @Test
//    @DisplayName("Testing get using MockMVC")
//    public void testGetUsingMvc() throws Exception {
//        when(employeeService.getAllEmployees()).thenReturn(employeeDtoList);
//        mockMvc.perform(get("/api/employee").accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
//    }
//
////    @Test
////    @DisplayName("Testing post Using MockMvc")
////    public void testPostUsingMockMvc() throws Exception {
////
////        when(employeeMapper.employeeDtoToEmployee(any())).thenReturn(testRequest);
////        when(employeeRepository.save(any())).thenReturn(testRequest);
////        when(employeeMapper.employeeToEmployeeDto(any())).thenReturn(postResponse);
////        when(employeeService.saveEmployee(any())).thenReturn(postResponse);
////        mockMvc.perform(post("/employee").accept(MediaType.APPLICATION_JSON))
////                .andExpect(status().isOk());
////
////    }
//
//    @Test
//    @DisplayName("Posting Employee Tested Successfully")
//    public void saveEmployeeTest() {
//
//        when(employeeService.saveEmployee(any())).thenReturn(postResponse);
//        ResponseEntity<EmployeeDto> result = subject.saveEmployee(postRequest);
//        assertNotNull(result);
//        verify(employeeService).saveEmployee(stringArgumentCaptor.capture());
//        EmployeeDto value = stringArgumentCaptor.getValue();
//        assertNotNull(value);
//
//    }
//
//
//    @Test
//    @DisplayName("Getting Transient Object Tested Successfully")
//    public void transientRequestTest() {
//
//        when(mockStarterRegistry.startProcessGetResult(anyString(),anyString(),any(),anyMap())).thenReturn(mockProcessResult);
//        when(mockProcessResult.getTransientData().getValueMap()).thenReturn(mockResponse);
//
//        ResponseEntity<Object> result = subject.TransientRequest("Nikith");
//        assertNotNull(result);
//        assertEquals(HttpStatus.OK.value(),result.getStatusCodeValue());
//        verify(mockStarterRegistry).startProcessGetResult(anyString(),anyString(),any(),anyMap());
//        assertThat(mockResponse,hasKey("firstName"));
//        assertEquals(mockResponse,result.getBody());
//    }
//
//    @Test
//    @DisplayName("PutEmployee call tested Successfully")
//    public void editEmployeeTest(){
//
//        when(employeeService.editEmployee(any())).thenReturn(postResponse);
//        ResponseEntity<Object> result = subject.editEmployee(postRequest);
//        verify(employeeService).editEmployee(any());
//    }
//    private HashMap<String, Object> setDefaultHashMapVariables() {
//        HashMap<String, Object> map = new HashMap<String, Object>() {{
//            put("firstName", "Nikith");
//        }};
//        return map;
//    }
//
//    private HashMap<String, Object> setDefaultHashMapForResponse() {
//        HashMap<String, Object> map = new HashMap<String, Object>() {{
//            put("firstName","Nikith");
//            put("lastName","hjhjjk");
//            put("domain","development");
//        }};
//        return map;
//    }
//    private Employee toGenerateExceptionObjects(){
//        Employee sampleRequest = Employee.builder()
//                .firstName("hkhk")
//                .lastName("ih")
//                .employeeType("tester")
//                .build();
//        return sampleRequest;
//    }
//    private Employee toGenerateTestRequest() throws FileNotFoundException {
//        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
//
//        FileReader fileReader = new FileReader("src/test/resources/PostRequest.json");
//        postRequest = gson.fromJson(new JsonReader(fileReader), EmployeeDto.class);
//        testRequest = Employee.builder()
//                .firstName(postRequest.getFirstName())
//                .lastName(postRequest.getLastName())
//                .employeeType(postRequest.getEmployeeType())
//                .email(postRequest.getEmail())
//                .joiningDate(postRequest.getJoiningDate())
//                .address(Address.builder()
//                        .street(postRequest.getAddress().getStreet())
//                        .aptNum(postRequest.getAddress().getAptNum())
//                        .pinCode(postRequest.getAddress().getPinCode())
//                        .flatName(postRequest.getAddress().getFlatName())
//                        .build()).build();
//        return testRequest;
//    }
//    private EmployeeDto toGenerateResponse() throws FileNotFoundException {
//        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
//        FileReader fileReader = new FileReader("src/test/resources/PostResponse.json");
//        postResponse = gson.fromJson(new JsonReader(fileReader),EmployeeDto.class);
//        return postResponse;
//    }
//
//    private ResponseEntity<Object> toGenerateSampleResponse() throws FileNotFoundException {
//        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
//        FileReader fileReader = new FileReader("src/test/resources/PostResponse.json");
//        sampleResponse = gson.fromJson(new JsonReader(fileReader),Object.class);
//        return sampleResponse;
//    }
//    private ResponseEntity<Employee> toGenerateSampleRequest() throws FileNotFoundException {
//        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
//        FileReader fileReader = new FileReader("src/test/resources/PostRequest.json");
//        samRequest = gson.fromJson(new JsonReader(fileReader), EmployeeDto.class);
//        Employee samResponse = employeeMapper.employeeDtoToEmployee(samRequest);
//        return new ResponseEntity<Employee>(samResponse,HttpStatus.OK);
//    }
//    private List<EmployeeDto> toGenerateListOfEmployees() throws IOException {
//        List<EmployeeDto> employeeDtos = new ArrayList<>();
//        employeeDtos.add(EmployeeDto.builder()
//                .firstName("Nivososo")
//                .lastName("kdkd")
//                .email("nv@gmail.com").build());
//        return employeeDtos;
//
//    }
////    @AfterEach
////    void tearDown() {
////    }
//}
