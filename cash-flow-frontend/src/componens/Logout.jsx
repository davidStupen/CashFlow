const Logout = () => {
  const handlerClick = () => {
    localStorage.removeItem("token")
  }
  return(
    <div>
      <button onClick={handlerClick}>Log out</button>
    </div>
  )
}
export default Logout