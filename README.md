# Personalized Medicine Diagnosis App

## A clinic designed to tailor treatments for your specific genetic makeup. Unique treatments for a unique you.

**What will the application do?**

- Keep track of the patients that are registered with the clinic
- Keep track of the information for each patient including:
  - Age
  - Date of Birth
  - First and Last Name
  - Personal Health Number
  - Genomic Profile
  - Patient Notes
- Diagnose a patient of any potential genetic conditions based on their genome profile

**Who will use it?**

- Medical Practitioners (of Personalized Medicine)
- Medical Office Assistants
- Medical Tech Staff

**Why is this project of interest to you?**

I have always been interested in *personalized medicine*. I studied biochemistry as my first degree 
and I was highly interested in computer science and medicine. I learned that there was a field called 
Personalized Medicine that was the intersection of Biochemistry, Computer Science, and Medicine. I am hoping 
to learn more about the field by creating this project. I originally had the aspiration to start a company dedicated 
to treating patients using personalized medicine, but this has deviated. I hope to find a job that will allow me to 
develop software related to advancing the field of personalized medicine.

## User Stories
- As a user, I want to be able to add a new patient to my list of patients 
and specify their age, date of birth, first and last name, personal health number, genomic profile, 
and patient notes
- As a user, I want to be able to view the list of patients in my list of patients
- As a user, I want to be able to remove a patient from my list of patients
- As a user, I want to be able to update a patient inside the list of patients
- As a user, I want to be able to check if a patient is already inside a list of patients
- As a user, I want to be able to import a genomic profile for a patient using a text file
- As a user, I want to be able to add a new genetic condition to my list of genetic conditions and 
specify the name of the condition, the DNA sequence that is mutated, and the location of the 
mutation in the genome
- As a user, I want to be able to remove a genetic condition in my list of genetic conditions
- As a user, I want to be able to update a genetic condition in my list of genetic conditions
- As a user, I want to be able to determine if a patient has any of the 
genetic conditions in my list of genetic conditions
- As a user, I want to be able to view all the field information for a particular patient
- As a user, I want to be able to save the list of patients and conditions to file (if I choose to do so)
- As a user, I want to start the application and be prompted to reload the list of patients and conditions from
file to resume where it was left off at

# Instructions for Grader

- You can generate the first required action related to adding Xs to a Y by clicking 'Add New Patient' in the main window and
entering the required fields with valid inputs and then clicking "SUBMIT" and then "Yes"
- You can generate the second required action related to adding Xs to a Y by selecting a patient from the 
list of patients and clicking 'Remove Patient' and then clicking "Yes"
- You can locate my visual component by opening the app and looking at the image of DNA displayed on the main window
- You can save the state of my application by clicking the 'Save' button at the top of the window
- You can reload the state of my application by pressing "YES" whe prompted at the beginning of loading the app OR by
pressing the 'Load' button at the top of the window and then 'OK'

# Phase 4: Task 2
1. User adds a new patient to the clinic.
2. User removes an existing patient from the clinic.
3. User generates a diagnosis report for a patient.
4. User views a patient's information
5. User adds a new genetic condition to the clinic
6. User removes an existing genetic condition from the clinic
7. User views information on the genetic condition
8. User saves the clinic data.
9. User loads the most recent clinic data.

Example Log:  
Wed Nov 29 11:21:31 PST 2023  
Loaded the last saved clinic data.  
Wed Nov 29 11:21:31 PST 2023  
Added a new patient, Jeff Ho, to the list of patients.  
Wed Nov 29 11:21:31 PST 2023  
Added a new patient, Jeffrey, to the list of patients.  
Wed Nov 29 11:21:31 PST 2023  
Added a new patient, Dave, to the list of patients.  
Wed Nov 29 11:21:31 PST 2023  
Added a new condition, Genetic Condition A, to the list of conditions.  
Wed Nov 29 11:21:31 PST 2023  
Added a new condition, Genetic Condition B, to the list of conditions.  
Wed Nov 29 11:21:31 PST 2023  
Added a new condition, Genetic Condition C, to the list of conditions.  
Wed Nov 29 11:21:31 PST 2023  
Fetched the genome for the patient with PHN, 2310529181 from the data folder.  
Wed Nov 29 11:21:39 PST 2023  
Removed a patient with PHN, 2345678901, from the list of patients.  
Wed Nov 29 11:21:42 PST 2023  
Retrieved information about patient with PHN: 2310529181  
Wed Nov 29 11:21:44 PST 2023  
Retrieved a full diagnosis report for patient with PHN, 2310529181  
Wed Nov 29 11:22:04 PST 2023  
Added a new patient, Dave, to the list of patients.  
Wed Nov 29 11:22:10 PST 2023  
Removed the condition, Genetic Condition C, from the list of conditions.  
Wed Nov 29 11:22:22 PST 2023  
Added a new condition, Genetic Condition C, to the list of conditions.  
Wed Nov 29 11:22:24 PST 2023  
Retrieved information about the condition, Genetic Condition B  
Wed Nov 29 11:22:25 PST 2023  
Saved the clinic data.  

# Phase 4: Task 3
There are several things that I could refactor to make it better:
- Having a superclass for PatientList and ConditionList because they have similar properties of add/remove/view. This would 
save more time and effort when designing these classes because they could inherit the same implementations.
- I could refactor my PatientList and ConditionList to be static classes and design it to have an iterator instead since 
I know that there will always be only one PatientList and one ConditionList. This will make it easier to iterate through it.
It also removes the need for a Clinic class because I would be able to access PatientList and ConditionList anywhere I want.
instead of having to call a getter method to retrieve the list from PatientList/ConditionList and then iterating through it.
- I could improve cohesion in my PatientComponent and ConditionComponent classes by creating more helper methods instead of 
trying to pack everything into one function.