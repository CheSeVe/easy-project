Customer
1. getCustomer с существующим Id | +           
2. getCustomer c рандомным Id | +
3. getCustomer с не валидным Id | +
4. getAll без параметров | +
5. getAll с size > 100 | + 
6. getAll с size = 20 | +
7. getAll c page вне диапазона | +
8. getAll с фильтром по name | +
9. getAll с фильтром по surname | +
10. getAll с фильтром по email | +
11. getAll с фильтром по phoneNumber | +
12. addCustomer c валидными данными | +
13. addCustomer с невалидным email | +
14. putCustomer с валидными данными | +
15. putCustomer с невалидным phoneNumber | +
16. patchCustomer с blank name | +
17. patchCustomer c валидным значением | +
18. deleteCustomer с валидным Id | +
19. deleteCustomer с рандомным Id | +

Order
1. getOrder с существующим Id | +
2. getOrder с рандомным Id | +
3. getAll без параметров | +
4. getAll сортировкой по дате по убыванию | +
5. getAll с сортировкой по дате с не валидным значением | +
6. addOrder с валидными данными | +
7. addOrder c не валидным статусом | нужно ловить ошибку 
8. addOrder с рандомным customerId | +
9. changeStatus с валидным status | +
10. changeStatus с не валидным status | нужно ловить ошибку
11. deleteStatus с валидным Id | +
12. deleteStatus с не валидным Id | +
