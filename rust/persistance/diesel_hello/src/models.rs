use super::schema::companies;

#[derive(Queryable)]
pub struct Company {
    pub id: i32,
    pub name: String,
    pub purpose: Option<String>,
    pub philosofy: Option<String>,
}

#[derive(Insertable)]
#[table_name = "companies"]
pub struct NewCompany<'a> {
    pub name: &'a str,
    pub purpose: Option<&'a str>,
    pub philosofy: Option<&'a str>,
}