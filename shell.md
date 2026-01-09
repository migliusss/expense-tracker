### Add an expense with an expense category, description and amount.
add --category "Restaurant" --description "Lunch" --amount 20
add --category "Restaurant" --description "Dinner" --amount 50
add --description "Summer Clothes" --amount 60
add --category "Pet" --description "Cat" --amount 6000
add --category "Pet" --description "Cat Food" --amount 2000

### Update an expense.
update --id 4 --category "Pet" --description "New Cat" --amount 5000

### Delete an expense.
delete --id 2

### View all expenses.
list

### Filter expenses by category.
list --category "Pet"

### View a summary of all expenses.
summary

### View a summary of expenses for a specific month of current year.
summary --month 1

### Set budget for a month.
set budget --month 1 --monthlyBudget 2000

### Export expenses to a CSV file.
export