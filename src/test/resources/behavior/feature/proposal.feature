Feature: Proposal

  @ProposalPOST
  Scenario Outline: ProposalPOST - step <step_id>': Create Proposal with uuid '<uuid>' with the headers institution '<institution>' and organization '<organization>'
    Given the headers institution '<institution>' and organization '<organization>'
    And mock uuid '<uuid>'
    When running "POST" for path "/cards/v1/proposals/" with payload '<payload>'
    Then it should return <http_status_code> HTTP status code
    And the returned location header should be '/cards/v1/proposals/<uuid>'
    And the body of the response should be ''
    And the data stored in the proposal database with uuid '<uuid>' must be '<stored_data>'
    Examples:
      | step_id | institution | organization | uuid                                 | http_status_code | payload               | stored_data                                                                                                        |
      | 01      | 136         | 27           | 2d5939c1-f995-40f2-99ec-8456414576a3 | 201              | {"name": "Proposal1"} | {"uuid": "2d5939c1-f995-40f2-99ec-8456414576a3", "institution": "136", "organization": "27", "name": "Proposal1"}  |
      | 02      | 136         | 27           | 39acb1d3-31d4-4412-8466-a6900a2235b6 | 201              | {"name": "Proposal2"} | {"uuid": "39acb1d3-31d4-4412-8466-a6900a2235b6", "institution": "136", "organization": "27", "name": "Proposal2"}  |
      | 03      | 136         | 515          | 1c470eee-8402-4174-95a2-2ee6ac4c3d56 | 201              | {"name": "Proposal3"} | {"uuid": "1c470eee-8402-4174-95a2-2ee6ac4c3d56", "institution": "136", "organization": "515", "name": "Proposal3"} |

  @ProposalPUT
  Scenario Outline: ProposalPUT - step '<step_id>': Update Proposal with uuid '<uuid>'
    Given the headers institution '<institution>' and organization '<organization>'
    When running "PUT" for path "/cards/v1/proposals/<uuid>" with payload '<payload>'
    Then it should return <http_status_code> HTTP status code
    And the body of the response should be ''
    And the data stored in the proposal database with uuid '<uuid>' must be '<stored_data>'
    Examples:
      | step_id | institution | organization | uuid                                 | payload                | http_status_code | stored_data                                                                                                        |
      | 01      | 136         | 27           | 2d5939c1-f995-40f2-99ec-8456414576a3 | {"name": "Proposal10"} | 204              | {"uuid": "2d5939c1-f995-40f2-99ec-8456414576a3", "institution": "136", "organization": "27", "name": "Proposal10"} |

  @ProposalDELETE
  Scenario Outline: ProposalDELETE - step '<step_id>': Update Proposal with uuid '<uuid>'
    Given the headers institution '<institution>' and organization '<organization>'
    When running "DELETE" for path "/cards/v1/proposals/<uuid>" with payload ''
    Then it should return <http_status_code> HTTP status code
    And the body of the response should be ''
    And the data stored in the proposal database with uuid '<uuid>' must be '<stored_data>'
    Examples:
      | step_id | institution | organization | uuid                                 | http_status_code | stored_data |
      | 01      | 136         | 27           | 39acb1d3-31d4-4412-8466-a6900a2235b6 | 204              | null        |

  @ProposalGET
  Scenario Outline: ProposalGET - step '<step_id>': Delete Proposal with uuid '<uuid>'
    Given the headers institution '<institution>' and organization '<organization>'
    When running "GET" for path "/cards/v1/proposals/<uuid>" with payload ''
    Then it should return <http_status_code> HTTP status code
    And the proposal returned should be '<body>'
    And the data stored in the proposal database with uuid '<uuid>' must be '<stored_data>'
    Examples:
      | step_id | institution | organization | uuid                                 | http_status_code | body                                                                   | stored_data                                                                                                        |
      | 01      | 136         | 27           | 2d5939c1-f995-40f2-99ec-8456414576a3 | 200              | {"uuid": "2d5939c1-f995-40f2-99ec-8456414576a3", "name": "Proposal10"} | {"uuid": "2d5939c1-f995-40f2-99ec-8456414576a3", "institution": "136", "organization": "27", "name": "Proposal10"} |
      | 02      | 136         | 515          | 1c470eee-8402-4174-95a2-2ee6ac4c3d56 | 200              | {"uuid": "1c470eee-8402-4174-95a2-2ee6ac4c3d56", "name": "Proposal3"}  | {"uuid": "1c470eee-8402-4174-95a2-2ee6ac4c3d56", "institution": "136", "organization": "515", "name": "Proposal3"} |

  @ProposalLIST
  Scenario Outline: ProposalLIST - step '<step_id>': List Proposals with the headers institution '<institution>' and organization '<organization>'
    Given the headers institution '<institution>' and organization '<organization>'
    When running "GET" for path "/cards/v1/proposals/" with payload ''
    Then it should return <http_status_code> HTTP status code
    And the list of proposals returned should be '<body>'
    Examples:
      | step_id | institution | organization | http_status_code | body                                                                                                                 |
      | 01      | 136         | 27           | 200              | [{"uuid": "2d5939c1-f995-40f2-99ec-8456414576a3", "institution": "136", "organization": "27", "name": "Proposal10"}] |
      | 02      | 136         | 515          | 200              | [{"uuid": "1c470eee-8402-4174-95a2-2ee6ac4c3d56", "institution": "136", "organization": "515", "name": "Proposal3"}] |

  @ProposalExceptionsHeaders
  Scenario Outline: ProposalExceptionsHeaders - step '<step_id>': Validate header exceptions for http method '<http_method>' with the headers institution '<institution>' and organization '<organization>'
    Given the headers institution '<institution>' and organization '<organization>'
    When running '<http_method>' for path "/cards/v1/proposals/<uuid>" with payload '<payload>'
    Then it should return <http_status_code> HTTP status code
    And the body of the response should be '<body>'
    Examples:
      | step_id | institution | organization | http_method | http_status_code | uuid                                 | payload | body                                                                                           |
      | 01      | null        |              | POST        | 400              |                                      |         | {"messages":["institution header must be informed"]}                                           |
      | 02      | null        | null         | POST        | 400              |                                      |         | {"messages":["institution header must be informed"]}                                           |
      | 03      | null        | 27           | POST        | 400              |                                      |         | {"messages":["institution header must be informed"]}                                           |
      | 04      |             |              | POST        | 400              |                                      | {}      | {"messages":["institution header must not be blank", "organization header must not be blank"]} |
      | 05      |             | null         | POST        | 400              |                                      |         | {"messages":["organization header must be informed"]}                                          |
      | 06      |             | 27           | POST        | 400              |                                      | {}      | {"messages":["institution header must not be blank"]}                                          |
      | 07      | 136         |              | POST        | 400              |                                      | {}      | {"messages":["organization header must not be blank"]}                                         |
      | 08      | 136         | null         | POST        | 400              |                                      |         | {"messages":["organization header must be informed"]}                                          |
      | 09      | null        |              | PUT         | 400              | c4959611-f914-4245-b3ad-253d29cae1f9 |         | {"messages":["institution header must be informed"]}                                           |
      | 10      | null        | null         | PUT         | 400              | c4959611-f914-4245-b3ad-253d29cae1f9 |         | {"messages":["institution header must be informed"]}                                           |
      | 11      | null        | 27           | PUT         | 400              | c4959611-f914-4245-b3ad-253d29cae1f9 |         | {"messages":["institution header must be informed"]}                                           |
      | 12      |             |              | PUT         | 400              | c4959611-f914-4245-b3ad-253d29cae1f9 | {}      | {"messages":["institution header must not be blank", "organization header must not be blank"]} |
      | 13      |             | null         | PUT         | 400              | c4959611-f914-4245-b3ad-253d29cae1f9 |         | {"messages":["organization header must be informed"]}                                          |
      | 14      |             | 27           | PUT         | 400              | c4959611-f914-4245-b3ad-253d29cae1f9 | {}      | {"messages":["institution header must not be blank"]}                                          |
      | 15      | 136         |              | PUT         | 400              | c4959611-f914-4245-b3ad-253d29cae1f9 | {}      | {"messages":["organization header must not be blank"]}                                         |
      | 16      | 136         | null         | PUT         | 400              | c4959611-f914-4245-b3ad-253d29cae1f9 |         | {"messages":["organization header must be informed"]}                                          |
      | 09      | null        |              | DELETE      | 400              | c4959611-f914-4245-b3ad-253d29cae1f9 |         | {"messages":["institution header must be informed"]}                                           |
      | 10      | null        | null         | DELETE      | 400              | c4959611-f914-4245-b3ad-253d29cae1f9 |         | {"messages":["institution header must be informed"]}                                           |
      | 11      | null        | 27           | DELETE      | 400              | c4959611-f914-4245-b3ad-253d29cae1f9 |         | {"messages":["institution header must be informed"]}                                           |
      | 12      |             |              | DELETE      | 400              | c4959611-f914-4245-b3ad-253d29cae1f9 |         | {"messages":["institution header must not be blank", "organization header must not be blank"]} |
      | 13      |             | null         | DELETE      | 400              | c4959611-f914-4245-b3ad-253d29cae1f9 |         | {"messages":["organization header must be informed"]}                                          |
      | 14      |             | 27           | DELETE      | 400              | c4959611-f914-4245-b3ad-253d29cae1f9 |         | {"messages":["institution header must not be blank"]}                                          |
      | 15      | 136         |              | DELETE      | 400              | c4959611-f914-4245-b3ad-253d29cae1f9 |         | {"messages":["organization header must not be blank"]}                                         |
      | 16      | 136         | null         | DELETE      | 400              | c4959611-f914-4245-b3ad-253d29cae1f9 |         | {"messages":["organization header must be informed"]}                                          |
      | 09      | null        |              | GET         | 400              | c4959611-f914-4245-b3ad-253d29cae1f9 |         | {"messages":["institution header must be informed"]}                                           |
      | 10      | null        | null         | GET         | 400              | c4959611-f914-4245-b3ad-253d29cae1f9 |         | {"messages":["institution header must be informed"]}                                           |
      | 11      | null        | 27           | GET         | 400              | c4959611-f914-4245-b3ad-253d29cae1f9 |         | {"messages":["institution header must be informed"]}                                           |
      | 12      |             |              | GET         | 400              | c4959611-f914-4245-b3ad-253d29cae1f9 |         | {"messages":["institution header must not be blank", "organization header must not be blank"]} |
      | 13      |             | null         | GET         | 400              | c4959611-f914-4245-b3ad-253d29cae1f9 |         | {"messages":["organization header must be informed"]}                                          |
      | 14      |             | 27           | GET         | 400              | c4959611-f914-4245-b3ad-253d29cae1f9 |         | {"messages":["institution header must not be blank"]}                                          |
      | 15      | 136         |              | GET         | 400              | c4959611-f914-4245-b3ad-253d29cae1f9 |         | {"messages":["organization header must not be blank"]}                                         |
      | 16      | 136         | null         | GET         | 400              | c4959611-f914-4245-b3ad-253d29cae1f9 |         | {"messages":["organization header must be informed"]}                                          |
      | 09      | null        |              | GET         | 400              |                                      |         | {"messages":["institution header must be informed"]}                                           |
      | 10      | null        | null         | GET         | 400              |                                      |         | {"messages":["institution header must be informed"]}                                           |
      | 11      | null        | 27           | GET         | 400              |                                      |         | {"messages":["institution header must be informed"]}                                           |
      | 12      |             |              | GET         | 400              |                                      |         | {"messages":["institution header must not be blank", "organization header must not be blank"]} |
      | 13      |             | null         | GET         | 400              |                                      |         | {"messages":["organization header must be informed"]}                                          |
      | 14      |             | 27           | GET         | 400              |                                      |         | {"messages":["institution header must not be blank"]}                                          |
      | 15      | 136         |              | GET         | 400              |                                      |         | {"messages":["organization header must not be blank"]}                                         |
      | 16      | 136         | null         | GET         | 400              |                                      |         | {"messages":["organization header must be informed"]}                                          |

  @ProposalExceptions
  Scenario Outline: ProposalExceptions - step <step_id>': Validate exceptions for http method '<http_method>' with uuid '<uuid>' and payload '<payload>'
    Given the headers institution '<institution>' and organization '<organization>'
    When running '<http_method>' for path "/cards/v1/proposals/<uuid>" with payload '<payload>'
    Then it should return <http_status_code> HTTP status code
    And the body of the response should be ''
    Examples:
      | step_id | institution | organization | uuid                                 | http_method | http_status_code | payload              |
      | 01      | 136         | 27           | e4f5574d-a44a-407c-a79e-c38eb3d6270a | PUT         | 404              | {"name": "Proposal"} |
      | 02      | 136         | 27           | c4959611-f914-4245-b3ad-253d29cae1f9 | DELETE      | 404              |                      |
      | 03      | 136         | 27           | e4f5574d-a44a-407c-a79e-c38eb3d6270a | GET         | 404              |                      |
