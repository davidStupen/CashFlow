import {Chart as ChartJS, defaults} from "chart.js/auto"
import {Line} from "react-chartjs-2"
import { useEffect, useState } from "react"
import api from "../api"
import { useNavigate, useParams } from "react-router-dom"
const ChartPage = () => {
  const navigate = useNavigate()
  const {userId} = useParams()
  const [backendData, setBackendData] = useState([])
  useEffect(() => {
    const feth = async () => {
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
  }, [userId])
  return (
    <div className="main-chart-container">
      <div className="char-container">
        <h1 className="chart-heading">Chart of expenses</h1>
        <button className="new-btn-style" onClick={() => navigate("/main")}>home page</button>
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
  )
}
export default ChartPage
