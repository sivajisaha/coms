export const entity_create = {
  create_process_definition: {
    form_title: 'Add a new process definition',
    form_name: 'create_process',
    form_id: '1',
    form_data: {"id":"","code":"","version":"","definition":"","description":"","status":""},
    field_data : [
      { 'field_id': '2','field_name': 'code','parent_container':'', 'field_type': 'text', 'field_label': 'Code','default_value': ''},
      { 'field_id': '3','field_name': 'version','parent_container':'', 'field_type': 'text', 'field_label': 'Version','default_value': ''},
      { 'field_id': '4','field_name': 'definition','parent_container':'', 'field_type': 'text-area', 'field_label': 'Definition','default_value': ''},
      { 'field_id': '4','field_name': 'description','parent_container':'', 'field_type': 'text', 'field_label': 'Description','default_value': ''},
      { 'field_id': '4','field_name': 'status','parent_container':'', 'field_type': 'text', 'field_label': 'Status','default_value': ''}
    ],
    service: 'COMS-BPM-API',
    submit_operation: '/process/processdef/add'
  },
  create_json_schema_definition: {
    form_title: 'Add a new JSON Schema definition',
    form_name: 'create_json_schema',
    form_id: '1',
    form_data: {"process_definition_id":"","schema_name":"","schema_category":"","schema_definition":""},
    field_data : [
      { 'field_id': '2','field_name': 'process_definition_id','parent_container':'', 'field_type': 'text', 'field_label': 'Process definition id','default_value': ''},
      { 'field_id': '3','field_name': 'schema_name','parent_container':'', 'field_type': 'text', 'field_label': 'Schema name','default_value': ''},
      { 'field_id': '4','field_name': 'schema_category','parent_container':'', 'field_type': 'text', 'field_label': 'Schema category','default_value': ''},
      { 'field_id': '4','field_name': 'schema_definition','parent_container':'', 'field_type': 'text-area', 'field_label': 'Schema definition','default_value': ''}
    ],
    service: 'COMS-BPM-API',
    submit_operation: '/process/jsonschemadef/add'
  },
  register_user: {
      form_title: 'Register',
      form_name: 'register_user',
      form_id: '5',
      form_data: {"first_name":"","last_name":"","login_id":"","user_password":""},
      field_data : [
        { 'field_id': '1','field_name': 'login_id','parent_container':'', 'field_type': 'text', 'field_label': 'Login email','default_value': 'Login email' },
        { 'field_id': '2','field_name': 'first_name','parent_container':'', 'field_type': 'text', 'field_label': 'First Name','default_value': 'First Name'},
        { 'field_id': '3','field_name': 'last_name','parent_container':'', 'field_type': 'text', 'field_label': 'Last Name','default_value': 'Last Name'},
        { 'field_id': '4','field_name': 'user_password','parent_container':'', 'field_type': 'text', 'field_label': 'Password','default_value': 'password'}
      ],
      service: 'COMS-USER-API',
      submit_operation: '/registeruser'
    },
    apply_loan: {
      form_title: 'Apply Loan',
      form_name: 'apply_loan',
      form_id: '6',
      form_data: {"loan_type":"","loan_amount":"","loan_duration":"","first_name":"","last_name":"","email_id":"","mobile_no":"","home_address":"","passport_no":"","bank_account_no":""},
      field_data : [
        { 'field_id': '1','field_name': 'loan_type','parent_container':'', 'field_type': 'dropdown', 'field_label': 'Loan Type','validation_error_message':'Please select loan type','default_value': '[{"label":"select", "value":"-1"},{"label":"Home Loan", "value":"HL"},{"label":"Vehicle Loan", "value":"VL"},{"label":"Personal Loan", "value":"PL"}]'},
        { 'field_id': '2','field_name': 'loan_amount','parent_container':'', 'field_type': 'text', 'field_label': 'Loan Amount','validation_error_message':'Please provide loan amount','default_value': ''},
        { 'field_id': '2','field_name': 'loan_duration','parent_container':'', 'field_type': 'text', 'field_label': 'Loan Duration','validation_error_message':'Please provide loan duration','default_value': ''},
        { 'field_id': '3','field_name': 'first_name','parent_container':'', 'field_type': 'text', 'field_label': 'First Name','validation_error_message':'Please provide first name','default_value': '' },
        { 'field_id': '4','field_name': 'last_name','parent_container':'', 'field_type': 'text', 'field_label': 'Last Name','validation_error_message':'Please provide last name','default_value': ''},
        { 'field_id': '5','field_name': 'email_id','parent_container':'', 'field_type': 'text', 'field_label': 'Email Id','validation_error_message':'Please provide email id','default_value': ''},
        { 'field_id': '6','field_name': 'mobile_no','parent_container':'', 'field_type': 'text', 'field_label': 'Mobile Number','validation_error_message':'Please provide mobile no','default_value': ''},
        { 'field_id': '7','field_name': 'home_address','parent_container':'', 'field_type': 'text', 'field_label': 'Home Address','validation_error_message':'Please provide home address','default_value': ''},
        { 'field_id': '8','field_name': 'passport_no','parent_container':'', 'field_type': 'text', 'field_label': 'Passport Number','validation_error_message':'Please provide passport number','default_value': ''},
        { 'field_id': '9','field_name': 'bank_account_no','parent_container':'', 'field_type': 'text', 'field_label': 'Bank Account No','validation_error_message':'Please provide bank account details','default_value': ''}
      ],
      service: 'COMS-CLIENT-API',
      submit_operation: '/loan/apply_loan'
    }
  };

  export const entity_all_view = {   
    view_all_process_definition: {
      form_title: 'View all process definition',
      form_name: 'view_all_process_defintion',
      entity_name: 'process_definition',
      form_id: '42',
      column_header_data: ["Id","Code","Version", "Description"],
      column_name_data: ["id","code","version","description"],
      column_id_column:"id",
      service: 'COMS-BPM-API',
      extract_operation: '/process/processdef/all',
      extract_operation_param_needed: 'n',
      form_create_header: 'Create new process definition',
      form_create_page: 'ProcessDefinitionCreateForm',
      action_pages:[{action:"View",action_page:"SingleProcessDefinitionView"},{action:"Edit",action_page:"EditProcessDefinition"},{action:"Task assignment",action_page:"TaskAssignment"},{action:"Instances",action_page:"ProcesInstanceAllView"}]
    },
    view_all_json_schema_definition: {
      form_title: 'View all json schema definition',
      form_name: 'view_all_json_schema_defintion',
      entity_name: 'json_schema_definition',
      form_id: '43',
      column_header_data: ["Id","Process Definition Id","Schema Name", "Schema Category"],
      column_name_data: ["id","process_definition_id","schema_name","schema_category"],
      column_id_column:"id",
      service: 'COMS-BPM-API',
      extract_operation: '/process/jsonschemadef/all',
      extract_operation_param_needed: 'n',
      form_create_header: 'Create new JSON Schema',
      form_create_page: 'JSONSchemaDefinitionCreateForm',
      action_pages:[{action:"View",action_page:"SingleJSONSchemaDefinitionView"},{action:"Edit",action_page:"EditJSONSchemaDefinition"}]
    },
    view_all_process_instances: {
      form_title: 'View all process instances',
      form_name: 'view_all_process_instances',
      entity_name: 'process_instances',
      form_id: '43',
      column_header_data: ["Id","Status"],
      column_name_data: ["id","status"],
      column_id_column:"id",
      service: 'COMS-BPM-API',
      extract_operation: '/process/instance/',
      extract_operation_param_needed: 'y',
      form_create_header: '',
      form_create_page: '',
      action_pages:[{action:"Track",action_page:"ProcesInstanceSingleView"}]
    },
    view_all_process_elements: {
      form_title: 'View process instance',
      form_name: 'view_process_single_instance',
      entity_name: 'process_single_instance',
      form_id: '44',
      column_header_data: ["Id","Step","Step type","Status","Transaction"],
      column_name_data: ["id","element_code","element_type","status","last_transaction_str"],
      column_id_column:"id",
      service: 'COMS-BPM-API',
      extract_operation: '/process/instance/elements/',
      extract_operation_param_needed: 'y',
      form_create_header: '',
      form_create_page: '',
      action_pages:[{action:"Process",action_page:"ActionOnActivity"}]
    }
  };
  export const entity_single_view = {
   
    view_single_process_defintion: {
      form_title: 'View process defintion',
      form_name: 'view_single_process_definition',
      form_id: '2',
      field_data : [
        { 'field_id': '1','field_name': 'id','field_label': 'Process Id'},
        { 'field_id': '2','field_name': 'code', 'field_label': 'Process Code'},
        { 'field_id': '3','field_name': 'version', 'field_label': 'Version'},
        { 'field_id': '4','field_name': 'description','field_label': 'Description'},
        { 'field_id': '5','field_name': 'definition', 'field_label': 'Defintion'}
      ],
      column_id_column:"id",
      form_edit_header:"Edit process defintion",
      service: 'COMS-BPM-API',
      dataload_operation: '/process/processdef/',
      edit_form:'EditProcessDefinition'
    },
    view_single_json_schema_defintion: {
      form_title: 'View json schema defintion',
      form_name: 'view_single_json_schema_defintion',
      form_id: '2',
      field_data : [
        { 'field_id': '1','field_name': 'id','field_label': 'Id'},
        { 'field_id': '2','field_name': 'process_definition_id', 'field_label': 'Process Definition Id'},
        { 'field_id': '3','field_name': 'schema_name', 'field_label': 'Schema name'},
        { 'field_id': '4','field_name': 'schema_category','field_label': 'Schema category'},
        { 'field_id': '5','field_name': 'schema_definition', 'field_label': 'Schema definition'}
      ],
      column_id_column:"id",
      form_edit_header:"Edit json schema defintion",
      service: 'COMS-BPM-API',
      dataload_operation: '/process/jsonschemadef/',
      edit_form:'EditJSONSchemaDefinition'
    }
  };
  export const entity_edit = {
    edit_process_definition: {
      form_title: 'Edit Process Definition',
      form_name: 'edit_process_definition',
      form_id: '3',
      form_data: {"id":"","code":"","version":"","description":"","definition":""},
      field_data : [
        { 'field_id': '1','field_name': 'id','parent_container':'', 'field_type': 'label', 'field_label': 'Id','default_value': 'Id' },
        { 'field_id': '2','field_name': 'code','parent_container':'', 'field_type': 'text', 'field_label': 'Code','default_value': 'Code' },
        { 'field_id': '3','field_name': 'version','parent_container':'', 'field_type': 'text', 'field_label': 'Version','default_value': 'Version'},
        { 'field_id': '4','field_name': 'description','parent_container':'', 'field_type': 'text', 'field_label': 'Description','default_value': 'Description'},
        { 'field_id': '5','field_name': 'definition','parent_container':'', 'field_type': 'text_area', 'field_label': 'Definition','default_value': 'Definition'}
      ],
      service: 'COMS-BPM-API',
      dataload_operation: '/process/processdef/',
      submit_operation: '/process/processdef/edit'
    },
    edit_json_schema_definition: {
      form_title: 'Edit json jchema Definition',
      form_name: 'edit_json_schema_definition',
      form_id: '3',
      form_data: {"id":"","process_definition_id":"","schema_name":"","schema_category":"","schema_definition":""},
      field_data : [
        { 'field_id': '1','field_name': 'id','parent_container':'', 'field_type': 'label', 'field_label': 'Id','default_value': 'Id' },
        { 'field_id': '2','field_name': 'process_definition_id','parent_container':'', 'field_type': 'text', 'field_label': 'process_definition_id','default_value': 'Code' },
        { 'field_id': '3','field_name': 'schema_name','parent_container':'', 'field_type': 'text', 'field_label': 'schema_name','default_value': 'Version'},
        { 'field_id': '4','field_name': 'schema_category','parent_container':'', 'field_type': 'text', 'field_label': 'schema_category','default_value': 'Description'},
        { 'field_id': '5','field_name': 'schema_definition','parent_container':'', 'field_type': 'text_area', 'field_label': 'schema_definition','default_value': 'Definition'}
      ],
      service: 'COMS-BPM-API',
      dataload_operation: '/process/jsonschemadef/',
      submit_operation: '/process/jsonschemadef/edit'
    }
  };
  export const breadcrumb_data=[
    {
      page_id:"ProcesInstanceSingleView",
      bread_crumb_urls:
      [
        {"sequence:":1,"uri_title":"Home","link":"false","target_page":""},
        {"sequence:":2,"uri_title":"All Process Instances","link":"true","target_page":""},
        {"sequence:":3,"uri_title":"Track process instance","link":"false","target_page":""}
      ]
    }
   ] ;