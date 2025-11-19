import {Chart as ChartJS, defaults} from "chart.js/auto"
import {Line} from "react-chartjs-2"
import { useEffect, useState } from "react"
import api from "../api"
import { useNavigate } from "react-router-dom"
import { jwtDecode } from "jwt-decode"
const ChartPage = () => {
  const navigate = useNavigate()
  const token = localStorage.getItem("token")
  const userId = jwtDecode(token).userId
  const [backendData, setBackendData] = useState([])
  useEffect(() => {
    const feth = async () => {
      console.log(userId)
      if(userId > -1){
        try{
          const response = await api.get(`/api/cash/chart/${userId}`)
          setBackendData(response.data)
        } catch(err){
          console.error(err)
        }
      }
    }
    feth()
  }, [])
  if(backendData.length > 0){
    return (
      <div>
        <button className="new-btn-style" onClick={() => navigate("/main")}>home page</button>
        <div className="main-chart-container">
          <div className="char-container">
            <h1 className="chart-heading">Chart of expenses</h1>
            <Line
              data={{
                labels: backendData.map(item => item.date),
                datasets: [{
                  label: "expenses",
                  data: backendData.map(item => item.amount),
                  backgroundColor: "purple",
                  borderColor: "purple"
                }]
              }}
            />
          </div>
        </div>
      </div>
    )
  } else{
    return(
      <div>
        <button className="new-btn-style" onClick={() => navigate("/main")}>home page</button>
      </div>
    )
  }
}
export default ChartPage
